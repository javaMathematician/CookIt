package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dish implements Serializable {
    private static final long serialVersionUID = 1049835430954235L;

    private String name;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("breadcrumbs")
    private List<String> categories;

    @SerializedName("recipeIngredient")
    private List<String> recipeIngredients;
    private List<List<String>> recipeInstructions;

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<List<String>> getRecipeInstructions() {
        return recipeInstructions;
    }

    public List<String> getRecipeImageURLs() {
        List<String> output = new ArrayList<>();

        for (List<String> list : recipeInstructions) {
            output.add(list.get(0));
        }

        return output;
    }

    public List<String> getRecipeTextInstructions() {
        List<String> output = new ArrayList<>();

        for (List<String> recipeInstruction : recipeInstructions) {
            output.add(recipeInstruction.get(1));
        }

        return output;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", image='" + imageURL + '\'' +
                ", breadcrumbs=" + categories +
                ", recipeIngredient=" + recipeIngredients +
                ", recipeInstructions=" + recipeInstructions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dish dish = (Dish) o;

        if (!name.equals(dish.name)) {
            return false;
        }

        if (!imageURL.equals(dish.imageURL)) {
            return false;
        }

        if (!categories.equals(dish.categories)) {
            return false;
        }

        if (!recipeIngredients.equals(dish.recipeIngredients)) {
            return false;
        }

        return recipeInstructions.equals(dish.recipeInstructions);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + imageURL.hashCode();
        result = 31 * result + categories.hashCode();
        result = 31 * result + recipeIngredients.hashCode();
        result = 31 * result + recipeInstructions.hashCode();
        return result;
    }
}
