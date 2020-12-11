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

        ArrayList<String> textInCards = new ArrayList<>(cards.keySet());
        ArrayList<String> imagesInCards = new ArrayList<>(cards.values());

        for (int i = 0; i < textInCards.size(); i++) {
            CardView generated = (CardView) inflater.inflate(R.layout.card, allDishesCardHolder, false);

            TextView textOnCard = (TextView) generated.findViewById(R.id.textOnCard);
            textOnCard.setText(textInCards.get(i));
            textOnCard.setTypeface(customFont);

            ImageView imageOnCard = (ImageView) generated.findViewById(R.id.imageOnCard);

            allDishesCardHolder.addView(generated);
        }

    }
}
