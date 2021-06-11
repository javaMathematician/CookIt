package org.slovenlypolygon.recipes.components.entitys;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;

public class Component implements Comparable<Component> {
    private final ComponentType componentType;
    private final String imageURL;
    private final String name;
    private final int id;

    private boolean isSelected;

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

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("componentType", componentType)
                .add("imageURL", imageURL)
                .add("name", name)
                .toString();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;
        return id == component.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public int compareTo(Component o) {
        return name.compareTo(o.name);
    }
}
