package org.slovenlypolygon.recipes.backend.mainobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

    private String name;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("breadcrumbs")
    private List<String> categories;

    @SerializedName("recipeIngredient")
    private List<String> recipeIngredients;
    private List<List<String>> recipeInstructions;

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