package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.utils.Converters;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DishCardInfoText extends AppCompatTextView {
    public DishCardInfoText(Context context, String dishName) {
        super(context);

        // заменяет только кавычки тех блюд, которые стоят в них целиком. то есть "<<наполеон>>" заменит, а "торт <<наполеон>>" — нет
        if (dishName.startsWith("«") && dishName.endsWith("»")) {
            dishName = dishName.replaceAll("(^«|»$)", "");
        }

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(Converters.fromDP(context, 220), WRAP_CONTENT);
        layoutParams.bottomToBottom = R.id.toolbar;
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        this.setLayoutParams(layoutParams);
        this.setWidth(Converters.fromDP(context, 220));
        this.setText(dishName);
        this.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        this.setTextColor(ContextCompat.getColor(context, R.color.cardTextColor));
    }
}
