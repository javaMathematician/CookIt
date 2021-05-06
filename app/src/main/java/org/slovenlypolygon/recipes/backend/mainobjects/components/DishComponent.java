package org.slovenlypolygon.recipes.backend.mainobjects.components;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public abstract class DishComponent implements Parcelable, Comparable<DishComponent> {
    protected final String name;
    protected final String imageURL;
    protected boolean selected;

    public DishComponent(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public DishComponent(Parcel parcel) {
        this.name = parcel.readString();
        this.imageURL = parcel.readString();
        this.selected = true; // если вызвали этот конструктор, значит, передали ингредиент на активность составления блюд. значит, он (ингредиент) заведомо выбран
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
    @NonNull
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

    @Override
    public int compareTo(DishComponent o) {
        return this.name.compareTo(o.name);
    }
}
