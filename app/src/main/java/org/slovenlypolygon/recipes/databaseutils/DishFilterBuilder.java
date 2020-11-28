package org.slovenlypolygon.recipes.databaseutils;

import java.util.ArrayList;
import java.util.List;

public class DishFilterBuilder {
    private final List<Dish> assortment;

    private String name;
    private List<String> categories;
    private List<String> recipeIngredients;

    public DishFilterBuilder(List<Dish> assortment) {
        this.name = null;
        this.categories = null;
        this.recipeIngredients = null;
        this.assortment = assortment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setRecipeIngredients(List<String> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Dish> getMatchingList() {
        List<Dish> dishList = new ArrayList<>();

        for (Dish dish : assortment) {
            boolean passedName = (name == null) || dish.getName().toLowerCase().contains(name.toLowerCase());
            boolean passedCategories = (categories == null) || listContains(categories, dish.getCategories());
            boolean passedIngredients = (recipeIngredients == null) || listContains(recipeIngredients, dish.getRecipeIngredients());

            if (passedName && passedCategories && passedIngredients) {
                dishList.add(dish);
            }
        }

        return dishList;
    }

    private String joinList(String delimiter, List<String> list) {
        StringBuilder builder = new StringBuilder();

        for (String string : list) {
            builder.append(string).append(delimiter);
        }

        return builder.substring(0, builder.length() - delimiter.length());
    }

    private boolean listContains(List<String> requiredList, List<String> allOfDishList) {
        String allOfDishString = joinList(", ", allOfDishList).toLowerCase().trim();

        for (String required : requiredList) {
            if (!allOfDishString.contains(required.toLowerCase().trim())) {
                return false;
            }
        }

        return true;
    }
}
