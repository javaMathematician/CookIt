package org.slovenlypolygon.recipes.backend.mainobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Category implements Parcelable {
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private final String name;
    private final String imageURL;
    private boolean selected;

    public Category(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public Category(Parcel parcel) {
        this.name = parcel.readString();
        this.imageURL = parcel.readString();
        this.selected = true; // если вызвали этот конструктор, значит, передали категорию на активность составления блюд. значит, она (категория) заведомо выбран
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

        Category that = (Category) o;
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
