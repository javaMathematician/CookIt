package org.slovenlypolygon.recipes.backend.backendcards;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.slovenlypolygon.recipes.R;

import java.util.ArrayList;
import java.util.Map;

public class CreateCards {
    public CreateCards(Map<String, String> cards, LinearLayout allDishesCardHolder, Typeface customFont, LayoutInflater inflater) {
        for (String textInCard : cards.keySet()) {
            CardView generated = (CardView) inflater.inflate(R.layout.card, allDishesCardHolder, false);

            TextView textOnCard = generated.findViewById(R.id.textOnCard);
            textOnCard.setText(textInCard);
            textOnCard.setTypeface(customFont);

            ImageView imageOnCard = generated.findViewById(R.id.imageOnCard);

            allDishesCardHolder.addView(generated);
        }

    }
}
