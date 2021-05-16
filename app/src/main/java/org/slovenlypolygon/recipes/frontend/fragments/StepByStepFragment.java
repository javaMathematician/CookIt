package org.slovenlypolygon.recipes.frontend.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.common.base.Joiner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Step;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class StepByStepFragment extends AbstractFragment {
    private LinearLayout linearLayout;
    private ImageView imageView;
    private Dish dish;
    private TextView dirtyIngredients;

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    private void addSteps() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Step step : dish.getSteps()) {
            CardView cardView = (CardView) inflater.inflate(R.layout.step_by_step_card, linearLayout, false);

            Button expandButton = cardView.findViewById(R.id.expandStepButton);
            TextView stepText = cardView.findViewById(R.id.stepText);
            ImageView imageView = cardView.findViewById(R.id.stepByStepImage);
            ConstraintLayout constraintLayout = cardView.findViewById(R.id.expandableStep);

            String url = step.getImageURL();

            if (url != null) {
                Picasso picasso = Picasso.get();
                picasso.setIndicatorsEnabled(false);
                picasso.load(url)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .fit()
                        .centerCrop()
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                picasso.setIndicatorsEnabled(false);
                                picasso.load(url)
                                        .error(R.drawable.ic_error_image)
                                        .fit()
                                        .centerCrop()
                                        .into(imageView);
                            }
                        });

                expandButton.setVisibility(View.VISIBLE);
                cardView.setOnClickListener(v -> {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.VISIBLE);
                        expandButton.setBackgroundResource(R.drawable.expandable_arrow_up);
                    } else {
                        constraintLayout.setVisibility(View.GONE);
                        expandButton.setBackgroundResource(R.drawable.expandable_arrow_down);
                    }
                });
            } else {
                stepText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            stepText.setText(step.getText());
            linearLayout.addView(cardView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_by_step_fragment, container, false);

        imageView = rootView.findViewById(R.id.dishStepByStepImage);
        linearLayout = rootView.findViewById(R.id.stepByStepLinearLayout);
        dirtyIngredients = rootView.findViewById(R.id.stepByStepIngredients);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView.setVisibility(View.GONE);
        dish = ((MainActivity) Objects.requireNonNull(getActivity())).getDaoFacade().getRichDish(dish);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(dish.getName());

        setupPreparedFrontend();
    }

    private void setupPreparedFrontend() {
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(dish.getImageURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(dish.getImageURL())
                                .error(R.drawable.ic_error_image)
                                .fit()
                                .centerCrop()
                                .into(imageView);
                    }
                });
        addDirtyIngredients();
        addSteps();
        addEmptySpace();
    }

    private void addDirtyIngredients() {
        String ingredients = getResources().getString(R.string.you_will_need) + "\n    " + Joiner.on(",\n    ").join(
                dish.getDirtyIngredients()
                        .stream()
                        .sorted(Comparator.comparing(String::length).reversed())
                        .collect(Collectors.toList())
        ) + ".";

        dirtyIngredients.setText(ingredients);
    }

    private void addEmptySpace() {
        View bottomEmptySpace = new View(getContext());
        bottomEmptySpace.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2
        ));

        linearLayout.addView(bottomEmptySpace);
    }

    @Override
    protected void searchTextChanged(String newText) {
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.GONE);
    }
}
