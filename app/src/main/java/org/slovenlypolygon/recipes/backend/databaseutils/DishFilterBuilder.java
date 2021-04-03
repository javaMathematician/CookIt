package org.slovenlypolygon.recipes.backend.databaseutils;

import org.slovenlypolygon.recipes.backend.mainobjects.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.util.List;

public class DishFilterBuilder {
    private String name;
    private List<Dish> assortment;
    private List<Category> categories;
    private List<Ingredient> recipeIngredients;

    public DishFilterBuilder setAssortment(List<Dish> assortment) {
        this.assortment = assortment;
        return this;
    }

    public DishFilterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DishFilterBuilder setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public DishFilterBuilder setRecipeIngredients(List<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public DishFilter createDishFilter() {
        DishFilter dishFilter = new DishFilter(assortment);
        dishFilter.setRecipeIngredients(recipeIngredients);
        dishFilter.setRecipeCategories(categories);
        dishFilter.setName(name);

        return dishFilter;
    }
}