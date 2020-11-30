package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.slovenlypolygon.recipes.databaseutils.Dish;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
