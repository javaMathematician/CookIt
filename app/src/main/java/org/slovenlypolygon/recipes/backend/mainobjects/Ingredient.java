package org.slovenlypolygon.recipes.backend.mainobjects;

import com.google.common.base.Objects;

public class Ingredient {
    private final String name;
    private final String imageURL;

    public Ingredient(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
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
}
