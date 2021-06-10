package org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;

public class Step {
    private String text;
    private String imageURL;

    public Step(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("imageURL", imageURL)
                .toString();
    }
}
