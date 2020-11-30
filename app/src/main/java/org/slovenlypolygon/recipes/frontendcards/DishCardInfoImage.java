package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import org.slovenlypolygon.recipes.utils.Converters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DishCardInfoImage extends AppCompatImageView {
    public DishCardInfoImage(Context context, String imageURL) {
        super(context);

        int margin = Converters.fromDP(context, 8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        layoutParams.weight = 1;

        this.setLayoutParams(layoutParams);
        this.setImageResource(android.R.drawable.ic_menu_compass);
    }
}
