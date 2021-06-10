package org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.computervision.OCR;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Component;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.DishesFragment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class BillScanFragment extends Fragment {
    private Set<String> parsed = new HashSet<>();
    private EasyImage easyImage;
    @Nullable private ProgressDialog progressDialog;
    @Nullable private AlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        easyImage = new EasyImage.Builder(requireContext())
                .setCopyImagesToPublicGalleryFolder(false)
                .allowMultiple(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bill_scan_fragment, container, false);
        setRetainInstance(true);

        CardView camera = inflate.findViewById(R.id.cameraOpener);
        CardView gallery = inflate.findViewById(R.id.galleryOpener);

        camera.setOnClickListener(t -> easyImage.openCameraForImage(this));
        gallery.setOnClickListener(t -> easyImage.openGallery(this));

        return inflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(@NotNull MediaFile[] mediaFiles, @NotNull MediaSource mediaSource) {
                Bitmap bitmap = BitmapFactory.decodeFile(mediaFiles[0].getFile().getAbsolutePath());
                parsed = new HashSet<>();

                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(requireContext(), ((MainActivity) requireActivity()).getCurrentTheme().equals("Dark") ? R.style.DarkProgressDialog : R.style.LightProgressDialog);
                progressDialog = new ProgressDialog(contextThemeWrapper);
                progressDialog.setTitle(getString(R.string.parsing));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(4);
                progressDialog.show();

                Disposable disposable = OCR.parseImage(bitmap)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(strings -> {
                            progressDialog.incrementProgressBy(1);
                            parsed.addAll(strings);
                        }, throwable -> {
                            Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            if (alertDialog != null) alertDialog.dismiss();

                            alertDialog = null;
                        }, () -> {
                            if (alertDialog != null) alertDialog.dismiss();

                            alertDialog = null;
                            startSearching();
                        });

                progressDialog.setOnCancelListener(dialog -> {
                    if (!disposable.isDisposed()) disposable.dispose();
                    progressDialog = null;
                });
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                Toast.makeText(requireContext(), R.string.incorrect_photo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSearching() {
        DishComponentDAO dishComponentDAO = ((DatabaseFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag("databaseFragment"))).getDishComponentDAO();
        Set<Component> foundComponents = new HashSet<>();

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle(getString(R.string.searching_ingredients));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(1270); // TODO: 03.06.2021 ЗАХАРДКОДИЛ ЧИСЛО, ПОТОМУ ЧТО ЛЕНЬ ОБРАЩАТЬСЯ В ДАО ТОЛЬКО РАДИ ЭТОГО
        progressDialog.show();

        dishComponentDAO.getAllComponents()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(component -> {
                    progressDialog.incrementProgressBy(1);

                    if (parsed.stream().anyMatch(string -> component.getName().toLowerCase().contains(string))) {
                        foundComponents.add(component);
                    }
                }, throwable -> {
                    Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    progressDialog = null;
                }, () -> {
                    progressDialog.dismiss();
                    progressDialog = null;

                    if (foundComponents.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                    } else {
                        String title = getString(R.string.parsed_successfull);
                        String message = getString(R.string.found_following_componenets) + " " +
                                Joiner.on(", ").join(foundComponents.parallelStream().map(Component::getName).collect(Collectors.toSet()));

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
}