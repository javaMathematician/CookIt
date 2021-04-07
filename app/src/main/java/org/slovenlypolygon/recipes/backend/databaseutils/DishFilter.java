package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.common.base.Joiner;

import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.PictureDishComponent;

import java.util.ArrayList;
import java.util.List;

public class DishFilter {
    private final List<Dish> assortment;

    private String name;
    private List<PictureDishComponent> recipeIngredients;

    public DishFilter(List<Dish> assortment) {
        this.name = null;
        this.recipeIngredients = null;
        this.assortment = assortment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComponents(List<PictureDishComponent> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Dish> getMatchingList() {
        List<Dish> dishList = new ArrayList<>();

        for (Dish dish : assortment) {
            boolean passedName = name == null || dish.getName().toLowerCase().contains(name.toLowerCase());
            boolean passedIngredients = recipeIngredients == null || containsAny(recipeIngredients, dish.getRecipeIngredients());

            if (passedName && passedIngredients) {
                dishList.add(dish);
            }
        }

        return dishList;
    }

    private boolean containsAny(List<PictureDishComponent> required, List<String> provided) {
        String allOfDishString = Joiner.on(", ").join(provided).toLowerCase().trim();
        return required.stream().map(PictureDishComponent::getName).anyMatch(t -> allOfDishString.contains(t.toLowerCase().trim()));
    }
}
