package org.slovenlypolygon.recipes.backend.mainobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dish implements Parcelable {
    public static final Parcelable.Creator<Dish> CREATOR = new Parcelable.Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel source) {
            return new Dish(source);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    private final String name;

    @SerializedName("image")
    private final String imageURL;

    @SerializedName("breadcrumbs")
    private final List<String> categories = new ArrayList<>();

    @SerializedName("recipeIngredient")
    private final List<String> recipeIngredients = new ArrayList<>();
    private final List<List<String>> recipeInstructions = new ArrayList<>();

    public Dish(Parcel parcel) {
        this.name = parcel.readString();
        this.imageURL = parcel.readString();

        parcel.readList(categories, String.class.getClassLoader());
        parcel.readList(recipeIngredients, String.class.getClassLoader());
        parcel.readList(recipeInstructions, List.class.getClassLoader());
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

    public List<List<String>> getRecipeInstructions() {
        return recipeInstructions;
    }

    public List<String> getRecipeImageURLs() {
        return recipeInstructions.parallelStream().map(t -> t.get(0)).collect(Collectors.toList());
    }

    public List<String> getRecipeTextInstructions() {
        return recipeInstructions.parallelStream().map(t -> t.get(1)).collect(Collectors.toList());
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("imageURL", imageURL)
                .add("categories", categories)
                .add("recipeIngredients", recipeIngredients)
                .add("recipeInstructions", recipeInstructions)
                .toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageURL);
        dest.writeList(categories);
        dest.writeList(recipeIngredients);
        dest.writeList(recipeInstructions);
    }
}
