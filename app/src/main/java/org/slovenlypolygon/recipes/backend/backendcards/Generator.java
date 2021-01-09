package org.slovenlypolygon.recipes.backend.backendcards;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.squareup.picasso.Picasso;
import org.slovenlypolygon.recipes.R;

import java.util.*;

public class Generator {
    public static Map<String, Boolean> checkedCards = new HashMap<>();
    private Map<String, String> dirtyToCleanedMapper;
    private Map<String, String> ingredientURLMapper;
    private LayoutInflater inflater;
    private Typeface customFont;
    private Context context;
    private ViewGroup root;

    public Generator(LayoutInflater inflater) {
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

    public void setRoot(ViewGroup root) {
        this.root = root;
    }

    public void setCustomFont(Typeface customFont) {
        this.customFont = customFont;
    }

    public List<CardView> generate() {
        Set<String> values = new TreeSet<>(dirtyToCleanedMapper.values());
        List<CardView> generated = new ArrayList<>();

        for (String ingredientName : values) {
            CardView currentCard = (CardView) inflater.inflate(R.layout.card, root, false);
            CheckBox checkBox = currentCard.findViewById(R.id.checkBoxOnCard);
            TextView textOnCard = currentCard.findViewById(R.id.textOnCard);
            ImageView image = currentCard.findViewById(R.id.imageOnCard);

            Uri uri = Uri.parse(ingredientURLMapper.getOrDefault(ingredientName, "https://sun9-65.userapi.com/c858328/v858328616/230711/on7eTEmN6rs.jpg"));
            Picasso.with(context).load(uri).resize(200, 200).centerCrop().into(image);
            checkedCards.put(ingredientName, false);

            textOnCard.setText(ingredientName);
            textOnCard.setTypeface(customFont);
            generated.add(currentCard);

            currentCard.setOnClickListener(t -> {
                checkBox.setChecked(!checkBox.isChecked());
                checkedCards.put(ingredientName, checkBox.isChecked());
            });

            if (generated.size() > 30) { // TODO: 08.01.2021 DISABLE LIMIT
                break;
            }
        }

        return generated;
    }
}
