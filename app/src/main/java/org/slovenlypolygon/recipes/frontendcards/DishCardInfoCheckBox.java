package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.utils.Converters;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DishCardInfoCheckBox extends AppCompatCheckBox {
    public DishCardInfoCheckBox(Context context) {
        super(context);

        this.setLayoutParams(new FrameLayout.LayoutParams(Converters.fromDP(context, 75), WRAP_CONTENT, Gravity.CENTER));
        this.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.white));
        this.setScaleX(2);
        this.setScaleY(2);
    }
}