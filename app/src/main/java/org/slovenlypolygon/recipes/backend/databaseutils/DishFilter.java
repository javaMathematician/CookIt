package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.common.base.Joiner;

import org.slovenlypolygon.recipes.backend.mainobjects.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class DishFilter {
    private final List<Dish> assortment;

    private String name;
    private List<Category> recipeCategories;
    private List<Ingredient> recipeIngredients;

    public DishFilter(List<Dish> assortment) {
        this.name = null;
        this.recipeIngredients = null;
        this.assortment = assortment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipeIngredients(List<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipeCategories(List<Category> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }

    public List<Dish> getMatchingList() {
        List<Dish> dishList = new ArrayList<>();

        for (Dish dish : assortment) {
            boolean passedName = name == null || dish.getName().toLowerCase().contains(name.toLowerCase());
            boolean passedIngredients = recipeIngredients == null || containsAnyIngredient(recipeIngredients, dish.getRecipeIngredients());
            boolean passedCategories = recipeCategories == null || containsAnyCategory(recipeCategories, dish.getCategories());

            if (passedName && passedIngredients && passedCategories) {
                dishList.add(dish);
            }
        }

        return dishList;
    }

    private boolean containsAnyIngredient(List<Ingredient> required, List<String> provided) {
        String allOfDishString = Joiner.on(", ").join(provided).toLowerCase().trim();
        return required.stream().map(Ingredient::getName).anyMatch(t -> allOfDishString.contains(t.toLowerCase().trim()));
    }

    private boolean containsAnyCategory(List<Category> required, List<String> provided) {
        String allOfDishString = Joiner.on(", ").join(provided).toLowerCase().trim();
        return required.stream().map(Category::getName).anyMatch(t -> allOfDishString.contains(t.toLowerCase().trim()));
    }
}
