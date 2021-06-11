package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.google.common.base.Joiner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Step;
import org.slovenlypolygon.recipes.frontend.FrontendDish;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class StepByStepFragment extends AbstractFragment {
    private final FrontendDish dish;
    private ImageView imageView;
    private DishComponentDAO dao;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private TextView dirtyIngredients;
    private ImageButton favoritesButton;
    private LinearLayout ingredientsLinearLayout;

    @Nullable private AlertDialog alertDialog;

    public StepByStepFragment(FrontendDish dish) {
        this.dish = dish;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        dao = ((DatabaseFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_frament_tag)))).getDishComponentDAO();
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
        ingredientsLinearLayout = rootView.findViewById(R.id.stepByStepIngredientLinearLayout);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView.setVisibility(View.GONE);
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(dish.getName());

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

        favoritesButton.setBackground(ResourcesCompat.getDrawable(getResources(), dao.containsFavorites(dish) ? R.drawable.in_favorites : R.drawable.add_to_favorites, null));
        favoritesButton.setOnClickListener(v -> {
            if (dao.containsFavorites(dish)) {
                dao.removeFromFavorites(dish);
                favoritesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.add_to_favorites, null));
                favoritesButton.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.scale));
                Toast.makeText(requireContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();
            } else {
                dao.addToFavorites(dish);
                favoritesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.in_favorites, null));
                favoritesButton.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.scale));
                Toast.makeText(requireContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
            }
        });

        dirtyIngredients.setOnClickListener(v -> {
            final boolean containsShoppingList = dao.containsShoppingList(dish);
            String[] actions = {containsShoppingList ? getString(R.string.delete_from_shopping_lists) : getString(R.string.add_to_shopping_lists)};

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_dialog, R.id.dialogTextView, actions);
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(requireContext(), ((MainActivity) requireActivity()).getCurrentTheme().equals("Dark") ? R.style.DarkProgressDialog : R.style.LightProgressDialog);

            alertDialog = new AlertDialog.Builder(contextThemeWrapper).setTitle(R.string.actions_with_shopping_list).setAdapter(arrayAdapter, (dialog1, which) -> {}).create();
            alertDialog.getListView().setOnItemClickListener((parent, view, position, id) -> {
                if (containsShoppingList) {
                    dao.removeFromShoppingList(dish);
                    Toast.makeText(requireContext(), getString(R.string.removed_from_shopping_list), Toast.LENGTH_SHORT).show();
                } else {
                    dao.addToShoppingList(dish);
                    Toast.makeText(requireContext(), getString(R.string.added_to_shopping_list), Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
                alertDialog = null;
            });

            alertDialog.setOnCancelListener(dialog -> alertDialog = null);
            alertDialog.show();
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

    private void addSteps() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());

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
                        scrollView.smoothScrollBy(0, cardView.getWidth() + cardView.getHeight()); // преполагаем, что картинка будет квадратной, поэтому скроллим на в том числе ширину карточки
                        constraintLayout.setVisibility(View.VISIBLE);
                        expandButton.setBackgroundResource(R.drawable.expandable_arrow_up);
                    } else {
                        scrollView.smoothScrollBy(0, -cardView.getHeight());
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

    private void addEmptySpace() {
        View bottomEmptySpace = new View(requireContext());
        bottomEmptySpace.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Resources.getSystem().getDisplayMetrics().heightPixels / 3
        ));

        linearLayout.addView(bottomEmptySpace);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View view = new View(requireContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Resources.getSystem().getDisplayMetrics().heightPixels / 3
            ));

            ingredientsLinearLayout.addView(view);
        }
    }

    @Override
    protected void searchTextChanged(String newText) {
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.GONE);

        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
