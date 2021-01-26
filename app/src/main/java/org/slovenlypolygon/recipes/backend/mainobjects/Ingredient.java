package org.slovenlypolygon.recipes.backend.mainobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Ingredient implements Parcelable {
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private final String name;
    private final String imageURL;
    private boolean selected;

    public Ingredient(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public Ingredient(Parcel parcel) {
        this.name = parcel.readString();
        this.imageURL = parcel.readString();
        this.selected = true; // если вызвали этот конструктор, значит, передали ингредиент на активность составления блюд. значит, он (ингредиент) заведомо выбран
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ingredient that = (Ingredient) o;
        return Objects.equal(name, that.name) && Objects.equal(imageURL, that.imageURL);
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
                .add("selected", selected)
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
    }
}
