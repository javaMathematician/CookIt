package org.slovenlypolygon.recipes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import org.slovenlypolygon.recipes.backend.backendcards.Generator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button changeView;
    private Generator generator;
    private Typeface customFont;
    private SearchView searchView;
    private LayoutInflater inflater;
    private List<CardView> generated;
    private TextView topTextViewOnToolbar;
    private LinearLayout allDishesCardHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        searchView = findViewById(R.id.searchView);
        allDishesCardHolder = findViewById(R.id.allDishesCardHolder);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);
        changeView = findViewById(R.id.changeView);
        searchView.setOnClickListener(v -> searchView.setIconified(false));
        inflater = LayoutInflater.from(this);

        customFont = Typeface.createFromAsset(getAssets(), "fonts/17651.ttf");
        topTextViewOnToolbar.setTypeface(customFont);
        changeView.setTypeface(customFont);
        generator = new Generator(inflater);

        try {
            generator.setIngredientURLMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.urls)));
            generator.setDirtyToCleanedMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));
            generator.setCustomFont(customFont);
            generator.setRoot(allDishesCardHolder);
            generated = generator.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CardView cardView : generated) {
            allDishesCardHolder.addView(cardView);
        }
    }
}