package org.slovenlypolygon.recipes;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.slovenlypolygon.recipes.databaseutils.Dish;
import org.slovenlypolygon.recipes.utils.Converters;

public class DishCard extends CardView {
    private String tag;
    private Dish dish;

    public DishCard(@NonNull Context context, Dish dish, int index) {
        super(context);

        tag = "CardView" + index;
        this.dish = dish;

        CardView cardView = this;

        int layoutHeight = Converters.fromDPToPixels(context, 130);
        int layoutMargin = Converters.fromDPToPixels(context, 10);
        int padding = Converters.fromDPToPixels(context, 100);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, layoutHeight);

        params.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        cardView.setLayoutParams(params);
        cardView.setPadding(padding, padding, padding, padding);
        cardView.setCardBackgroundColor(R.color.searchViewColorWhite);
        cardView.setRadius(Converters.fromDPToPixels(context, 6));
    }
}
