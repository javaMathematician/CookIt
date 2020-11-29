package org.slovenlypolygon.recipes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.slovenlypolygon.recipes.databaseutils.Dish;
import org.slovenlypolygon.recipes.utils.Converters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DishCard extends CardView {
    private String tag;
    private Dish dish;


    public DishCard(@NonNull Context context, Dish dish, int index) {
        super(context);

        tag = "CardView" + index;
        this.dish = dish;

        CardView cardView = this;

        int layoutHeight = Converters.fromDPToPixels(context, 130);
        int layoutMargin = Converters.fromDPToPixels(context, 10);
        int padding = Converters.fromDPToPixels(context, 100);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, layoutHeight);
        params.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        cardView.setLayoutParams(params);
        cardView.setPadding(padding, padding, padding, padding);
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardBackgroundColorDark));
        cardView.setRadius(Converters.fromDPToPixels(context, 6));

        //Вставить картинку

        TextView productNameInCards = new TextView(context);
        productNameInCards.setText(dish.getName().replace("«", "").replace("»", ""));
        //dish.getName надо заменить на название продута
        productNameInCards.setTextSize(30);
        productNameInCards.setTextColor(ContextCompat.getColor(context, R.color.cardTextColor));
        cardView.addView(productNameInCards);

        //поправить CheckBox
        CheckBox productSelection = new CheckBox(context);
        productSelection.setScaleX(2);
        productSelection.setScaleY(2);
        productSelection.setBackgroundColor(ContextCompat.getColor(context, R.color.cardTextColor));
        //cardView.addView(productSelection);
    }
}
