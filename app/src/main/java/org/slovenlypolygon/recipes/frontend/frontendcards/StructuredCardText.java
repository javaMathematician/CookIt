package org.slovenlypolygon.recipes.frontend.frontendcards;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StructuredCardText extends AppCompatTextView {
    private final Context context;
    private String text;

    public StructuredCardText(Context context) {
        super(context);
        this.context = context;
//        заменяет только кавычки тех блюд, которые стоят в них целиком. то есть "<<наполеон>>" заменит, а "торт <<наполеон>>" — нет
//        if (text.startsWith("«") && text.endsWith("»")) {
//            text = text.replaceAll("(^«|»$)", "");
//        }
    }

    @Override
    public String getText() {
        return text;
    }

    public void setCardText(String text) {
        this.text = text;
    }

    public void makeContent() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        layoutParams.weight = 2;

        this.setLayoutParams(layoutParams);
        this.setText(text);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        this.setTextColor(ContextCompat.getColor(context, R.color.cardTextColor));
    }
}
