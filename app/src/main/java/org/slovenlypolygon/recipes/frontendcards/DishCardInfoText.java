package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DishCardInfoText extends AppCompatTextView {
    public DishCardInfoText(Context context, String dishName) {
        super(context);

        // заменяет только кавычки тех блюд, которые стоят в них целиком. то есть "<<наполеон>>" заменит, а "торт <<наполеон>>" — нет
        if (dishName.startsWith("«") && dishName.endsWith("»")) {
            dishName = dishName.replaceAll("(^«|»$)", "");
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        layoutParams.weight = 2;

        this.setLayoutParams(layoutParams);
        this.setText(dishName);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        this.setTextColor(ContextCompat.getColor(context, R.color.cardTextColor));
    }
}
