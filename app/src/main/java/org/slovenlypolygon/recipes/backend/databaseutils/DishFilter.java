package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.common.base.Joiner;

import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;

import java.util.ArrayList;
import java.util.List;

public class DishFilter {
    private final List<Dish> assortment;

    private String name;
    private List<DishComponent> dishComponents;

    public DishFilter(List<Dish> assortment) {
        this.name = null;
        this.dishComponents = null;
        this.assortment = assortment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComponents(List<DishComponent> dishComponents) {
        this.dishComponents = dishComponents;
    }

    public List<Dish> getMatchingList() {
        List<Dish> dishList = new ArrayList<>();

        for (Dish dish : assortment) {
            boolean passedName = name == null || dish.getName().toLowerCase().contains(name.toLowerCase());
            boolean passedIngredients = dishComponents == null || containsAny(dishComponents, dish.getRecipeIngredients());
            boolean passedCategories = dishComponents == null || containsAny(dishComponents, dish.getCategories());

            if (passedName && (passedIngredients || passedCategories)) {
                dishList.add(dish);
            }
        }

        return dishList;
    }

    private boolean containsAny(List<DishComponent> required, List<String> provided) {
        String allOfDishString = Joiner.on(", ").join(provided).toLowerCase().trim();
        return required.parallelStream().map(DishComponent::getName).anyMatch(t -> allOfDishString.contains(t.toLowerCase().trim()));
    }
}
