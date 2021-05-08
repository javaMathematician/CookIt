package org.slovenlypolygon.recipes.backend.mainobjects.components;

import androidx.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public abstract class DishComponent implements Comparable<DishComponent> {
    protected final String name;
    protected final String imageURL;
    protected boolean selected;

    public DishComponent(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
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
    public int compareTo(DishComponent o) {
        return this.name.compareTo(o.name);
    }
}
