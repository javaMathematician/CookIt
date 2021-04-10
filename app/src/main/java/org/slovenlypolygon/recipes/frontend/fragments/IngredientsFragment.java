package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;
import org.slovenlypolygon.recipes.backend.mainobjects.components.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class IngredientsFragment extends AbstractFragment {
    private final List<DishComponent> components = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private FloatingActionButton scrollToTop;
    private DishComponentAdapter dishComponentAdapter;

    private void initializeVariablesForIngredient(View rootView) {
        recyclerView = rootView.findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Dish> dishes = ((MainActivity) Objects.requireNonNull(getActivity())).getDishList();

        changeViewIngredient = rootView.findViewById(R.id.changeView);
        scrollToTop = rootView.findViewById(R.id.floatingActionButton);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });
        recyclerView.setRecyclerListener(holder -> {
            if (holder.getAdapterPosition() > 9) {
                scrollToTop.show();
            } else {
                scrollToTop.hide();
            }
        });
        scrollToTop.hide();

        Set<String> ingredientsSet = new TreeSet<>();
        Set<String> categoriesSet = new TreeSet<>();
        Map<String, String> ingredientURLMapper = ((MainActivity) Objects.requireNonNull(getActivity())).getIngredientURLMapper();
        Map<String, String> categoryURLMapper = ((MainActivity) getActivity()).getCategoryURLMapper();

        for (Dish dish : dishes) {
            ingredientsSet.addAll(dish.getRecipeIngredients());
            categoriesSet.addAll(dish.getCategories());
        }

        String errorPictureURL = "https://sun9-60.userapi.com/dylNRBX-QrACucpHbXaBlobPNfd0ihbv37SJkw/MZ9j1ew2xWA.jpg?ava=1";
        for (String ingredientName : ingredientsSet) {
            components.add(new Ingredient(ingredientName, ingredientURLMapper.getOrDefault(ingredientName, errorPictureURL)));
        }

        for (String categoryName : categoriesSet) {
            components.add(new Category(categoryName, categoryURLMapper.getOrDefault(categoryName, errorPictureURL)));
        }
    }

    @Override
    protected void searchTextChanged(String newText) {
        dishComponentAdapter.getFilter().filter(newText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);
        initializeVariablesForIngredient(rootView);

        // TODO: 08.04.2021 HERE YOU CAN CHANGE STARTPAGE
        dishComponentAdapter = new DishComponentAdapter(components.parallelStream().filter(t -> t instanceof Ingredient).collect(Collectors.toList()));
        recyclerView.setAdapter(dishComponentAdapter);
        changeViewIngredient.setOnClickListener(t -> {
            List<DishComponent> matching = components.parallelStream().filter(DishComponent::isSelected).collect(Collectors.toList());

            if (!matching.isEmpty()) {
                goToRecipes(matching, true);
            } else {
                Toast.makeText(getContext(), R.string.none_selected_ingredient, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void goToRecipes(List<DishComponent> selected, boolean highlight) {
        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setSelectedComponents(selected);
        dishesFragment.setHighlightSelected(highlight);

        Objects.requireNonNull(getFragmentManager())
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_holder, dishesFragment, "dishes")
                .addToBackStack(null)
                .commit();
    }

    public List<DishComponent> getAllIngredients() {
        return components;
    }

    public void clearSelectedComponents() {
        components.parallelStream().forEach(t -> t.setSelected(false));
        dishComponentAdapter.notifyDataSetChanged();
    }
}
