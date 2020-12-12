package org.slovenlypolygon.recipes.backend.backendcards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import org.slovenlypolygon.recipes.R;

import java.util.*;

public class Generator {
    private Map<String, String> ingredientURLMapper;
    private Map<String, String> dirtyToCleanedMapper;
    private LayoutInflater inflater;
    private ViewGroup root;
    private Typeface customFont;

    public Generator(LayoutInflater inflater) {
        this.inflater = inflater;
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

        int pressedColor = Color.parseColor("#5DA130");
        int unpressedColor = Color.parseColor("#222222");
        for (String textInCard : values) {
            CardView generate = (CardView) inflater.inflate(R.layout.card, root, false);
            CheckBox checkBox = generate.findViewById(R.id.checkBoxOnCard);
            LinearLayout linearLayout = generate.findViewById(R.id.backgroundOnCard);


            generate.setOnClickListener(v -> {
                checkBox.setChecked(!checkBox.isChecked());

                if (checkBox.isChecked()) linearLayout.setBackgroundColor(pressedColor);
                else linearLayout.setBackgroundColor(unpressedColor);
            });

            TextView textOnCard = generate.findViewById(R.id.textOnCard);
            textOnCard.setText(textInCard);
            textOnCard.setTypeface(customFont);

            ImageView imageOnCard = generate.findViewById(R.id.imageOnCard);

            generated.add(generate);
        }

        return generated;
    }
}
