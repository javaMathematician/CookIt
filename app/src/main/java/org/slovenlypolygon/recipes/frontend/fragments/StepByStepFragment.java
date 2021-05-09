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
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDirtyComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDish;
import org.slovenlypolygon.recipes.backend.rawobjects.RawStep;

import java.util.Objects;
import java.util.stream.Collectors;

public class StepByStepFragment extends AbstractFragment {
    private LinearLayout linearLayout;
    private RawDish dish;

    public void setDish(RawDish dish) {
        this.dish = dish;
    }

    private void addSteps() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (RawStep step : dish.getSteps()) {
            CardView cardView = (CardView) inflater.inflate(R.layout.step_by_step_card, linearLayout, false);

            Button expandButton = cardView.findViewById(R.id.expandStepButton);
            TextView stepText = cardView.findViewById(R.id.stepText);
            ImageView imageView = cardView.findViewById(R.id.stepByStepImage);
            ConstraintLayout constraintLayout = cardView.findViewById(R.id.expandableStep);

            String url = step.getStepImageURL();

            if (url != null) {
                Picasso.get()
                        .load(url)
                        .error(R.drawable.ic_error_image)
                        .into(imageView);

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

            stepText.setText(step.getStepText());
            linearLayout.addView(cardView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_by_step_fragment, container, false);

        linearLayout = rootView.findViewById(R.id.stepByStepLinearLayout);

        Picasso.get()
                .load(dish.getDishImageURL())
                .error(R.drawable.ic_error_image)
                .into((ImageView) rootView.findViewById(R.id.dishStepByStepImage));

        String ingredients = getResources().getString(R.string.you_will_need) + "\n    " + Joiner.on(",\n    ").join(
                dish.getDirtyComponents()
                        .stream()
                        .map(RawDirtyComponent::getContent)
                        .sorted((t, u) -> t.length() - u.length())
                        .collect(Collectors.toList())
        ) + ".";
        rootView.<TextView>findViewById(R.id.stepByStepIngredients).setText(ingredients.replace("---", "").replace("———", ""));

        addSteps();

        View bottomEmptySpace = new View(getContext());
        bottomEmptySpace.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2
        ));
        linearLayout.addView(bottomEmptySpace);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView.setVisibility(View.GONE);
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(dish.getDishName());
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
