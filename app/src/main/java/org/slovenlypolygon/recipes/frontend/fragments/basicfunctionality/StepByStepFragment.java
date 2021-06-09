package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.common.base.Joiner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Step;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;

import java.util.Comparator;
import java.util.stream.Collectors;

public class StepByStepFragment extends AbstractFragment {
    private Dish dish;
    private ImageView imageView;
    private DishComponentDAO dao;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private TextView dirtyIngredients;
    private ImageButton favoritesButton;
    private ImageButton addShoppingListButton;

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag("databaseFragment")).getDishComponentDAO();
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

            if (url != null && !url.isEmpty()) {
                Picasso picasso = Picasso.get();
                picasso.setIndicatorsEnabled(false);
                picasso.load(url)
                        .placeholder(R.drawable.loading_animation)
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
                                        .placeholder(R.drawable.loading_animation)
                                        .error(R.drawable.error_image)
                                        .fit()
                                        .centerCrop()
                                        .into(imageView);
                            }
                        });

                expandButton.setVisibility(View.VISIBLE);
                cardView.setOnClickListener(view -> {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.VISIBLE);
                        scrollView.smoothScrollTo(0, view.getTop());
                        expandButton.setBackgroundResource(R.drawable.expandable_arrow_up);
                    } else {
                        constraintLayout.setVisibility(View.GONE);
                        scrollView.smoothScrollBy(0, -view.getHeight());
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
        setRetainInstance(true);

        imageView = rootView.findViewById(R.id.dishStepByStepImage);
        scrollView = rootView.findViewById(R.id.stepByStepScrollView);
        linearLayout = rootView.findViewById(R.id.stepByStepLinearLayout);
        dirtyIngredients = rootView.findViewById(R.id.stepByStepIngredients);
        favoritesButton = rootView.findViewById(R.id.favoritesSwitcher);
        addShoppingListButton = rootView.findViewById(R.id.addListButton);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView.setVisibility(View.GONE);
        dish = dao.getRichDish(dish);
        activity.getSupportActionBar().setTitle(dish.getName());

        setupPreparedFrontend();
    }

    private void setupPreparedFrontend() {
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(dish.getImageURL())
                .placeholder(R.drawable.loading_animation)
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
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.error_image)
                                .fit()
                                .centerCrop()
                                .into(imageView);
                    }
                });

        favoritesButton.setBackground(dao.containsFavorites(dish) ? getResources().getDrawable(R.drawable.in_favorites) : getResources().getDrawable(R.drawable.add_to_favorites));
        favoritesButton.setOnClickListener(v -> {
            if (dao.containsFavorites(dish)) {
                dao.removeFromFavorites(dish);
                favoritesButton.setBackground(getResources().getDrawable(R.drawable.add_to_favorites));
                favoritesButton.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.scale));
                Toast.makeText(getContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();
            } else {
                dao.addToFavorites(dish);
                favoritesButton.setBackground(getResources().getDrawable(R.drawable.in_favorites));
                favoritesButton.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.scale));
                Toast.makeText(getContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
            }
        });

        addDirtyIngredients();
        addSteps();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            addEmptySpace();
        }
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
                Resources.getSystem().getDisplayMetrics().heightPixels / 3
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
