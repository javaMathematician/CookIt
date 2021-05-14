package org.slovenlypolygon.recipes.backend.mainobjects;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dish {
    private final String imageURL;
    private final String dishURL;
    private final String name;
    private final int id;

    private List<Step> steps = new ArrayList<>();
    private Set<String> dirtyIngredients = new HashSet<>();
    private Set<Component> cleanComponents = new HashSet<>();

    public Dish(int dishID, String dishName, String dishImageURL, String dishURL) {
        this.id = dishID;
        this.name = dishName;
        this.dishURL = dishURL;
        this.imageURL = dishImageURL;
    }

    public Dish(Dish other) {
        this(other.id, other.name, other.imageURL, other.dishURL);

        this.steps = other.steps;
        this.dirtyIngredients = other.dirtyIngredients;
        this.cleanComponents = other.cleanComponents;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Set<String> getDirtyIngredients() {
        return dirtyIngredients;
    }

    public void setDirtyIngredients(Set<String> dirtyIngredients) {
        this.dirtyIngredients = dirtyIngredients;
    }

    public Set<Component> getCleanComponents() {
        return cleanComponents;
    }

    public void setCleanComponents(Set<Component> cleanComponents) {
        this.cleanComponents = cleanComponents;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("imageURL", imageURL)
                .add("steps", steps)
                .add("components", dirtyIngredients)
                .toString();
    }
}
