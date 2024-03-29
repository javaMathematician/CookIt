package org.slovenlypolygon.cookit.billscanner;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.common.base.Joiner;

import org.slovenlypolygon.cookit.MainActivity;
import org.slovenlypolygon.cookit.R;
import org.slovenlypolygon.cookit.abstractfragments.SimpleCookItFragment;
import org.slovenlypolygon.cookit.backend.DatabaseFragment;
import org.slovenlypolygon.cookit.backend.DishComponentDAO;
import org.slovenlypolygon.cookit.components.entitys.Component;
import org.slovenlypolygon.cookit.dishes.fragments.DishesFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BillScanFragment extends SimpleCookItFragment {
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private final static int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 1035;
    private static final String APP_TAG = "CookIt";

    private ContextThemeWrapper contextThemeWrapper;
    private Set<String> parsed = new HashSet<>();
    private File photoFile;

    @Nullable private ProgressDialog progressDialog;
    @Nullable private AlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bill_scan_fragment, container, false);
        setRetainInstance(true);

        CardView camera = inflate.findViewById(R.id.cameraOpener);
        CardView gallery = inflate.findViewById(R.id.galleryOpener);

        camera.setOnClickListener(t -> onLaunchCamera());
        gallery.setOnClickListener(t -> onLaunchGallery());

        return inflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                processImage(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            } else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
                try (InputStream imageStream = requireContext().getContentResolver().openInputStream(Objects.requireNonNull(data).getData())) {
                    processImage(BitmapFactory.decodeStream(imageStream));
                } catch (IOException e) {
                    Toast.makeText(requireContext(), getString(R.string.something_wrong) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(requireContext(), R.string.photo_closed, Toast.LENGTH_SHORT).show();
        }
    }

    private void processImage(Bitmap bitmap) {
        RxJavaPlugins.setErrorHandler(throwable -> { });
        parsed = new HashSet<>();

        contextThemeWrapper = new ContextThemeWrapper(requireContext(), ((MainActivity) requireActivity()).getCurrentTheme().equals("Dark") ? R.style.DarkProgressDialog : R.style.LightProgressDialog);
        progressDialog = new ProgressDialog(contextThemeWrapper);
        progressDialog.setTitle(getString(R.string.parsing));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(4);
        progressDialog.show();

        Disposable disposable = OCR.parseImage(bitmap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> {
                    if (progressDialog != null) {
                        progressDialog.incrementProgressBy(1);
                    }

                    parsed.addAll(strings);
                }, throwable -> {
                    if (throwable instanceof UnknownHostException) {
                        Toast.makeText(requireContext(), R.string.internet_required, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    throwable.printStackTrace();

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }, () -> {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                    startSearching();
                });

        progressDialog.setOnCancelListener(dialog -> {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }

            progressDialog = null;
            Toast.makeText(requireContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
        });
    }

    private void startSearching() {
        DishComponentDAO dishComponentDAO = ((DatabaseFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag)))).getDishComponentDAO();
        Set<Component> foundComponents = new HashSet<>();

        progressDialog = new ProgressDialog(contextThemeWrapper);
        progressDialog.setTitle(getString(R.string.searching_ingredients));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(1270); // TODO: 03.06.2021 ЗАХАРДКОДИЛ ЧИСЛО, ПОТОМУ ЧТО ЛЕНЬ ОБРАЩАТЬСЯ В ДАО ТОЛЬКО РАДИ ЭТОГО
        progressDialog.show();

        dishComponentDAO.getAllComponents()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(component -> {
                    progressDialog.incrementProgressBy(1);

                    if (parsed.stream().anyMatch(string -> containsString(component, string))) {
                        foundComponents.add(component);
                    }
                }, throwable -> {
                    throwable.printStackTrace();

                    progressDialog.dismiss();
                    progressDialog = null;
                }, () -> {
                    progressDialog.dismiss();
                    progressDialog = null;

                    if (foundComponents.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                    } else {
                        String title = getString(R.string.parsed_successful);
                        String message = getString(R.string.found_following_components) + " " +
                                Joiner.on(", ").join(foundComponents.parallelStream().map(Component::getName).collect(Collectors.toSet())).toLowerCase();

                        String accept = getString(R.string.continueString);
                        String decline = getString(R.string.dismiss);

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                        builder.setTitle(title);
                        builder.setMessage(message);

                        builder.setPositiveButton(accept, (dialog, id) -> {
                            DishesFragment dishesFragment = new DishesFragment();
                            dishesFragment.setSelectedComponents(foundComponents);
                            dishesFragment.setHighlightSelected(true);

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                                    .replace(R.id.fragmentHolder, dishesFragment, "dishes")
                                    .addToBackStack(null)
                                    .commit();

                            alertDialog = null;
                        });

                        builder.setNegativeButton(decline, (dialog, id) -> alertDialog = null);

                        builder.setCancelable(true);
                        builder.setOnCancelListener(dialog -> alertDialog = null);

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
    }

    private boolean containsString(Component component, String string) {
        String componentName = component.getName().toLowerCase();

        return componentName.contains(string) || string.contains(componentName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchView searchView = requireActivity().findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

        for (Dialog dialog : Arrays.asList(progressDialog, alertDialog)) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        for (Dialog dialog : Arrays.asList(progressDialog, alertDialog)) {
            if (dialog != null) {
                dialog.show();
            }
        }
    }

    private void onLaunchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri();

        Uri fileProvider = FileProvider.getUriForFile(requireContext(), "org.slovenlypolygon.cookit.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void onLaunchGallery() {
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private File getPhotoFileUri() {
        File mediaStorageDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + "CookItBillPhoto.jpg");
    }
}