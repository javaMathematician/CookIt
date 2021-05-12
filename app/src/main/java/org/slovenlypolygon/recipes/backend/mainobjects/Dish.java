package org.slovenlypolygon.recipes.backend.mainobjects;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dish {
    private final int id;
    private String name;
    private String dishURL;
    private String imageURL;
    private List<Step> steps = new ArrayList<>();
    private Set<Component> components = new HashSet<>();
    private Set<String> cleanComponents = new HashSet<>();

    public Dish(int dishID, String dishName, String dishImageURL, String dishURL) {
        this.id = dishID;
        this.name = name;
        this.dishURL = dishURL;
        this.imageURL = imageURL;
    }

    public Set<String> getCleanComponents() {
        return cleanComponents;
    }

    public void setCleanComponents(Set<String> cleanComponents) {
        this.cleanComponents = cleanComponents;
    }

    public String getDishURL() {
        return dishURL;
    }

    public void setDishURL(String dishURL) {
        this.dishURL = dishURL;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
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
                .add("components", components)
                .toString();
    }
}
