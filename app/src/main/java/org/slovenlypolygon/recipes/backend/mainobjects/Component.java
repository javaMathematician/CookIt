package org.slovenlypolygon.recipes.backend.mainobjects;

import com.google.common.base.MoreObjects;

public class Component {
    private final int id;
    private ComponentType componentType;
    private String imageURL;
    private String name;

    public Component(int id, ComponentType componentType, String name, String imageURL) {
        this.id = id;
        this.componentType = componentType;
        this.imageURL = imageURL;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("componentType", componentType)
                .add("imageURL", imageURL)
                .add("name", name)
                .toString();
    }
}
