package org.slovenlypolygon.recipes.frontend.frontendcards;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.utils.Converters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StructuredCard extends CardView {
    private String imageURL;
    private String text;
    private Context context;

    public StructuredCard(@NonNull Context context) {
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

    public void setCardText(String text) {
        this.text = text;
    }

    public void makeContent() {
        int height = Converters.fromDP(context, 130);
        int margin = Converters.fromDP(context, 8);

        ConstraintLayout.LayoutParams cardParams = new ConstraintLayout.LayoutParams(MATCH_PARENT, height);
        cardParams.setMargins(margin, margin, margin, margin);
        cardParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        cardParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        this.setLayoutParams(cardParams);
        this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardBackgroundColorDark));
        this.setRadius(Converters.fromDP(context, 6));

        StructuredCardLinearLayout child = new StructuredCardLinearLayout(context);
        child.setImageURL(imageURL);
        child.setLinearLayoutText(text);
        child.makeContent();

        this.addView(child);
    }
}
