package org.slovenlypolygon.recipes.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtil;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;
import org.slovenlypolygon.recipes.backend.mainobjects.components.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class IngredientsFragment extends AbstractFragment {
    private List<Dish> dishes;
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private FloatingActionButton scrollToTop;
    private final List<DishComponent> components = new ArrayList<>();

    private void initializeVariablesForIngredient(View rootView) {
        recyclerView = rootView.findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            dishes = Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        try {
            Set<String> ingredientsSet = new TreeSet<>();
            Set<String> categoriesSet = new TreeSet<>();
            Map<String, String> ingredientURLMapper = new Gson().fromJson(IOUtil.toString(getResources().openRawResource(R.raw.ingredient_to_image_url)), new TypeToken<Map<String, String>>() {
            }.getType());
            Map<String, String> categoryURLMapper = new Gson().fromJson(IOUtil.toString(getResources().openRawResource(R.raw.category_to_image_url)), new TypeToken<Map<String, String>>() {
            }.getType());

            for (Dish dish : dishes) {
                ingredientsSet.addAll(dish.getRecipeIngredients());
                categoriesSet.addAll(dish.getCategories());
            }

            for (String ingredientName : ingredientsSet) {
                components.add(new Ingredient(ingredientName, ingredientURLMapper.getOrDefault(ingredientName, "https://sun9-60.userapi.com/dylNRBX-QrACucpHbXaBlobPNfd0ihbv37SJkw/MZ9j1ew2xWA.jpg?ava=1")));
            }

            for (String categoryName : categoriesSet) {
                components.add(new Category(categoryName, categoryURLMapper.getOrDefault(categoryName, "https://sun9-60.userapi.com/dylNRBX-QrACucpHbXaBlobPNfd0ihbv37SJkw/MZ9j1ew2xWA.jpg?ava=1")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);
        initializeVariablesForIngredient(rootView);

        DishComponentAdapter adapter = new DishComponentAdapter(components.stream().filter(t -> t instanceof Ingredient).collect(Collectors.toList())); // TODO: 08.04.2021 HERE YOU CAN CHANGE STARTPAGE
        recyclerView.setAdapter(adapter);
        changeViewIngredient.setOnClickListener(t -> {
            List<DishComponent> matching = components.stream().filter(DishComponent::isSelected).collect(Collectors.toList());

            if (!matching.isEmpty()) {
                goToRecipes(matching, true);
            } else {
                Toast.makeText(getContext(), R.string.none_selected_ingredient, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void goToRecipes(List<DishComponent> selected, boolean highlight) {
        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setSelectedComponents(selected);
        dishesFragment.setHighlightSelected(highlight);

        Objects.requireNonNull(getFragmentManager())
                .beginTransaction()
                .replace(R.id.fragment_holder, dishesFragment, "dishes")
                .addToBackStack(null)
                .commit();
    }
}
