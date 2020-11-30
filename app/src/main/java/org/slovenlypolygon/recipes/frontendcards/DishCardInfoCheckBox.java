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
        //this.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.white)); // TODO: 30.11.2020 FIX COLOR
        this.setScaleX(1.5f);
        this.setScaleY(1.5f);
    }
}