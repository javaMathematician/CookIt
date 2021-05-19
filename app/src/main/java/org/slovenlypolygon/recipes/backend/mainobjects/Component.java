package org.slovenlypolygon.recipes.backend.mainobjects;

import com.google.common.base.MoreObjects;

public class Component {
    private final ComponentType componentType;
    private final String imageURL;
    private final int id;

    private String name;

    public Component(int id, ComponentType componentType, String name, String imageURL) {
        this.componentType = componentType;
        this.imageURL = imageURL;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
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
