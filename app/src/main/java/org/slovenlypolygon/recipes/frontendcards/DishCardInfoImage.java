package org.slovenlypolygon.recipes.frontendcards;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.utils.Converters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DishCardInfoImage extends AppCompatImageView {
    public DishCardInfoImage(Context context, String imageURL) {
        super(context);

        int width = Converters.fromDP(context, 118);
        int margin = Converters.fromDP(context, 6);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, MATCH_PARENT);
        layoutParams.setMargins(margin, margin, margin, margin);

        this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_outline));
        this.setScaleType(ScaleType.FIT_START);
        this.setImageResource(android.R.drawable.ic_menu_compass);
    }
}
