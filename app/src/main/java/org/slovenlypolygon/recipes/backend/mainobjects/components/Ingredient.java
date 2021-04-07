package org.slovenlypolygon.recipes.backend.mainobjects.components;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient extends PictureDishComponent implements Parcelable {
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Ingredient(String name, String imageURL) {
        super(name, imageURL);
    }

    public Ingredient(Parcel parcel) {
        super(parcel);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
