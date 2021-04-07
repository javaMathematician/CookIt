package org.slovenlypolygon.recipes.backend.mainobjects.components;

import android.os.Parcel;
import android.os.Parcelable;

public class Category extends PictureDishComponent implements Parcelable {
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

    public Category(String name, String imageURL) {
        super(name, imageURL);
    }

    public Category(Parcel parcel) {
        super(parcel);
    }
}
