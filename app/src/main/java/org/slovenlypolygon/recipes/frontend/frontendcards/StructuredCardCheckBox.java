package org.slovenlypolygon.recipes.frontend.frontendcards;

import android.content.Context;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StructuredCardCheckBox extends AppCompatCheckBox {
    public StructuredCardCheckBox(Context context) {
        super(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        params.weight = .4f;

        this.setLayoutParams(params);
        this.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cardTextColor));
    }
}