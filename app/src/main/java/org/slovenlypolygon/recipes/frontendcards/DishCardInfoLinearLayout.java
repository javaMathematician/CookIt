package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import android.icu.number.Scale;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.WrapperListAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import org.slovenlypolygon.recipes.databaseutils.Dish;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DishCardInfoLinearLayout extends LinearLayout {
    public DishCardInfoLinearLayout(Context context, Dish dish) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(new ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        this.setGravity(Gravity.CENTER);
        this.addView(new DishCardInfoImage(context, dish.getImageURL()));
        this.addView(new DishCardInfoText(context, dish.getName()));
        this.addView(new DishCardInfoCheckBox(context));
    }
}
