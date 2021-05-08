package org.slovenlypolygon.recipes.backend.mainobjects;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Dish {
    private final String name;

    @SerializedName("image")
    private final String imageURL;

    @SerializedName("breadcrumbs")
    private final List<String> categories = new ArrayList<>();

    @SerializedName("recipeIngredient")
    private final List<String> recipeIngredients = new ArrayList<>();
    private final Map<String, String> recipeInstructions = new TreeMap<>();

    public Dish(Parcel parcel) {
        this.name = parcel.readString();
        this.imageURL = parcel.readString();

        parcel.readList(categories, String.class.getClassLoader());
        parcel.readList(recipeIngredients, String.class.getClassLoader());
        parcel.readMap(recipeInstructions, Map.class.getClassLoader());
    }

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

    public Map<String, String> getRecipeInstructions() {
        return recipeInstructions;
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
        return Objects.equal(name, dish.name) && Objects.equal(imageURL, dish.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, imageURL);
    }

    @Override
    @NonNull
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("imageURL", imageURL)
                .add("categories", categories)
                .add("recipeIngredients", recipeIngredients)
                .add("recipeInstructions", recipeInstructions)
                .toString();
    }
}
