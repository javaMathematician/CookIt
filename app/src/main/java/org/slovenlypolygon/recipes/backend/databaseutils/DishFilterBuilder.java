package org.slovenlypolygon.recipes.backend.databaseutils;

import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.util.List;

public class DishFilterBuilder {
    private List<Dish> assortment;
    private String name;
    private List<Ingredient> recipeIngredients;

    public DishFilterBuilder setAssortment(List<Dish> assortment) {
        this.assortment = assortment;
        return this;
    }

    public DishFilterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DishFilterBuilder setRecipeIngredients(List<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public DishFilter createDishFilter() {
        DishFilter dishFilter = new DishFilter(assortment);
        dishFilter.setRecipeIngredients(recipeIngredients);
        dishFilter.setName(name);

        return dishFilter;
    }
}