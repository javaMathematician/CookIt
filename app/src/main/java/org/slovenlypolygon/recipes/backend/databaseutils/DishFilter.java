package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.common.base.Joiner;

import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class DishFilter {
    private final List<Dish> assortment;

    private String name;
    private List<String> recipeCategories;
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

    public void setRecipeCategories(List<String> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }

    public List<Dish> getMatchingList() {
        List<Dish> dishList = new ArrayList<>();

        for (Dish dish : assortment) {
            boolean passedName = name == null || dish.getName().toLowerCase().contains(name.toLowerCase());
            boolean passedIngredients = recipeIngredients == null || caontainsAny(recipeIngredients, dish.getRecipeIngredients());
            boolean passedCategories = recipeCategories == null || caontainsAny(recipeIngredients, dish.getRecipeIngredients());

            if (passedName && passedIngredients && passedCategories) {
                dishList.add(dish);
            }
        }

        return dishList;
    }

    private boolean caontainsAny(List<Ingredient> required, List<String> provided) {
        String allOfDishString = Joiner.on(", ").join(provided).toLowerCase().trim(); // не Set, потому что надо искать подстроку подстроки
        // в частности, при запорсе "Мед", надо найти меды всех масс, ведь они хранятся в виде "Мед 20 грамм", "Мед 40 грамм", ...

        return required.stream().map(Ingredient::getName).anyMatch(t -> allOfDishString.contains(t.toLowerCase().trim()));
    }
}
