package org.slovenlypolygon.recipes.frontendcards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.databaseutils.Dish;
import org.slovenlypolygon.recipes.utils.Converters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("ViewConstructor")
public class DishCard extends CardView {
//    private String tag;
//    private Dish dish;

    public DishCard(@NonNull Context context, Dish dish, int index) {
        super(context);
//        tag = "CardView" + index;
//        this.dish = dish;
        int height = Converters.fromDP(context, 130);
        int margin = Converters.fromDP(context, 10);
        int padding = Converters.fromDP(context, 100);

        ConstraintLayout.LayoutParams cardParams = new ConstraintLayout.LayoutParams(MATCH_PARENT, height);
        cardParams.setMargins(margin, margin, margin, margin);
        cardParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        cardParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        this.setLayoutParams(cardParams);
        this.setPadding(padding, padding, padding, padding);
        this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardBackgroundColorDark));
        this.setRadius(Converters.fromDP(context, 6));
        this.addView(new DishCardInfoLinearLayout(context, dish));
    }
}
