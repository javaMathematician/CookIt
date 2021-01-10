package org.slovenlypolygon.recipes.backend.backendcards;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.squareup.picasso.Picasso;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;

import java.io.Serializable;
import java.util.*;

public class IngredientsGenerator {
    public static Map<String, Boolean> checkedCards = new HashMap<>();
    private Map<String, String> dirtyToCleanedMapper;
    private Map<String, String> ingredientURLMapper;
    private LayoutInflater inflater;
    private Typeface customFont;
    private Context context;

    public IngredientsGenerator(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIngredientURLMapper(Map<String, String> ingredientURLMapper) {
        this.ingredientURLMapper = ingredientURLMapper;
    }

    public void setDirtyToCleanedMapper(Map<String, String> dirtyToCleanedMapper) {
        this.dirtyToCleanedMapper = dirtyToCleanedMapper;
    }

    public void setCustomFont(Typeface customFont) {
        this.customFont = customFont;
    }

    public List<CardView> generateIngredients(ViewGroup root) {
        Set<String> values = new TreeSet<>(dirtyToCleanedMapper.values());
        List<CardView> generated = new ArrayList<>();

        Drawable selectedCard = ContextCompat.getDrawable(context, R.drawable.selected_card);
        Drawable regularCard = ContextCompat.getDrawable(context, R.drawable.regular_card);

        for (String ingredientName : values) {
            CardView currentCard = (CardView) inflater.inflate(R.layout.card_ingridient, root, false);
            CheckBox checkBox = currentCard.findViewById(R.id.checkBoxOnCard);
            LinearLayout layout = currentCard.findViewById(R.id.linearLayout);
            TextView textOnCard = currentCard.findViewById(R.id.textOnCard);
            ImageView image = currentCard.findViewById(R.id.imageOnCard);

            Picasso.with(context)
                    .load(Uri.parse(ingredientURLMapper.getOrDefault(ingredientName, "")))
                    .error(R.drawable.sample_dish_for_error)
                    .resize(200, 200)
                    .centerCrop()
                    .into(image);
            checkedCards.put(ingredientName, false);

            textOnCard.setText(ingredientName);
            textOnCard.setTypeface(customFont);
            generated.add(currentCard);

            currentCard.setOnClickListener(t -> {
                checkBox.setChecked(!checkBox.isChecked());
                checkedCards.put(ingredientName, checkBox.isChecked());

                layout.setBackground(checkBox.isChecked() ? selectedCard : regularCard);
                currentCard.setBackground(checkBox.isChecked() ? selectedCard : regularCard);
            });

            if (generated.size() > 3000) { // TODO: 08.01.2021 DISABLE LIMIT
                break;
            }
        }

        return generated;
    }
}
