package org.slovenlypolygon.recipes.frontend.frontendcards;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StructuredCardLinearLayout extends LinearLayout {
    private String imageURL;
    private String text;
    private Context context;

    public StructuredCardLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getText() {
        return text;
    }

    public void setLinearLayoutText(String text) {
        this.text = text;
    }

    public void makeContent() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(new ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        this.setGravity(Gravity.CENTER);
        StructuredCardImage image = new StructuredCardImage(context);
        image.setImageURL(imageURL);
        image.makeContent();

        StructuredCardText cardText = new StructuredCardText(context);
        cardText.setCardText(text);
        cardText.makeContent();

        this.addView(image);
        this.addView(cardText);
        this.addView(new StructuredCardCheckBox(context));
    }
}
