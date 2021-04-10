package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StepByStepFragment extends AbstractFragment {
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private Dish dish;

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    private void initialize(View rootView) {
        Map<String, List<String>> map = ((MainActivity) Objects.requireNonNull(getActivity())).getDishToRawIngredients();

        linearLayout = rootView.findViewById(R.id.stepByStepLinearLayout);
        scrollView = rootView.findViewById(R.id.stepByStepScrollView);

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .into((ImageView) rootView.findViewById(R.id.dishStepByStepImage));

        String ingredients = getResources().getString(R.string.you_will_need) + "\n    " + Joiner.on(",\n    ").join(Objects.requireNonNull(map.getOrDefault(dish.getName(), new ArrayList<>()))) + ".";
        ((TextView) rootView.findViewById(R.id.stepByStepIngredients)).setText(ingredients.replace("---", "").replace("———", ""));
    }

    private void addSteps() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (List<String> pair : dish.getRecipeInstructions()) {
            CardView cardView = (CardView) inflater.inflate(R.layout.step_by_step_card, linearLayout, false);

            Button expandButton = cardView.findViewById(R.id.expandStepButton);
            TextView stepText = cardView.findViewById(R.id.stepText);
            ImageView imageView = cardView.findViewById(R.id.stepByStepImage);
            ConstraintLayout constraintLayout = cardView.findViewById(R.id.expandableStep);

            String url = pair.get(0);
            String text = pair.get(1);

            if (url.length() != 0) {
                Picasso.get()
                        .load(url)
                        .error(R.drawable.sample_dish_for_error)
                        .into(imageView);

                expandButton.setVisibility(View.VISIBLE);
                cardView.setOnClickListener(v -> {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.VISIBLE);
                        expandButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    } else {
                        constraintLayout.setVisibility(View.GONE);
                        expandButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }

                    scrollView.fullScroll(View.FOCUS_DOWN);
                });
            } else {
                stepText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            stepText.setText(text);
            linearLayout.addView(cardView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_by_step_fragment, container, false);

        initialize(rootView);
        addSteps();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView.setVisibility(View.GONE);
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
