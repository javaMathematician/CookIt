package org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.computervision.OCR;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.DishesFragment;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class BillScanFragment extends Fragment {
    private Set<String> parsed = new HashSet<>();
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private EasyImage easyImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        easyImage = new EasyImage.Builder(getContext())
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

        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(@NotNull MediaFile[] mediaFiles, @NotNull MediaSource mediaSource) {
                Bitmap bitmap = BitmapFactory.decodeFile(mediaFiles[0].getFile().getAbsolutePath());
                parsed = new HashSet<>();

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle(getString(R.string.parsing));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(4);
                progressDialog.show();

                OCR.parseImage(bitmap)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(strings -> {
                            progressDialog.incrementProgressBy(1);
                            parsed.addAll(strings);
                        }, Throwable::printStackTrace, () -> {
                            progressDialog.dismiss();

                            String title = getString(R.string.parsed_successfull);
                            String message = getString(R.string.found_following_strings) + " " + Joiner.on(", ").join(parsed);
                            String accept = getString(R.string.continueString);
                            String decline = getString(R.string.dismiss);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(title);
                            builder.setMessage(message);

                            builder.setPositiveButton(accept, (dialog, id) -> {
                                sureStartSearching();
                                alertDialog = null;
                            });

                            builder.setNegativeButton(decline, (dialog, id) -> alertDialog = null);

                            builder.setCancelable(true);
                            alertDialog = builder.create();
                            alertDialog.show();
                            progressDialog = null;
                        });
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                Toast.makeText(getContext(), R.string.incorrect_photo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sureStartSearching() {
        DishComponentDAO dishComponentDAO = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag("databaseFragment")).getDishComponentDAO();
        Set<Integer> foundComponentIDs = new HashSet<>();

        progressDialog = new ProgressDialog(getContext());
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
                        foundComponentIDs.add(component.getId());
                    }
                }, Throwable::printStackTrace, () -> {
                    progressDialog.dismiss();

                    DishesFragment dishesFragment = new DishesFragment();
                    dishesFragment.setSelectedComponentIDs(foundComponentIDs);
                    dishesFragment.setHighlightSelected(true);

                    getParentFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                            .replace(R.id.fragmentHolder, dishesFragment, "dishes")
                            .addToBackStack(null)
                            .commit();
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchView searchView = getActivity().findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (progressDialog != null) {
            progressDialog.show();
        }

        if (alertDialog != null) {
            alertDialog.show();
        }
    }
}