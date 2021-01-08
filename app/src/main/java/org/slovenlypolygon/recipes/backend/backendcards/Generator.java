package org.slovenlypolygon.recipes.backend.backendcards;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import org.slovenlypolygon.recipes.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Generator {
    private Map<String, String> dirtyToCleanedMapper;
    private Map<String, String> ingredientURLMapper;
    private LayoutInflater inflater;
    private Typeface customFont;
    private ViewGroup root;

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

    public List<CardView> generate() throws MalformedURLException {
        Set<String> values = new TreeSet<>(dirtyToCleanedMapper.values());
        List<CardView> generated = new ArrayList<>();

        for (String textInCard : values) {
            CardView generate = (CardView) inflater.inflate(R.layout.card, root, false);
            CheckBox checkBox = generate.findViewById(R.id.checkBoxOnCard);
            TextView textOnCard = generate.findViewById(R.id.textOnCard);
            ImageView image = generate.findViewById(R.id.imageOnCard);

            URL url = new URL("http://im0-tub-ru.yandex.net/i?id=eb836ecc2ff1c13f24ebe0897c85c256&amp;n=13");

            generate.setOnClickListener(v -> {
                checkBox.setChecked(!checkBox.isChecked());
            });
            textOnCard.setText(textInCard);
            textOnCard.setTypeface(customFont);

            generated.add(generate);

            if (generated.size() > 30) { // TODO: 08.01.2021 DISABLE LIMIT
                break;
            }
        }

        return generated;
    }
}
