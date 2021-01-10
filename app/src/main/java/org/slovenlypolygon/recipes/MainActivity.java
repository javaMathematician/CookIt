package org.slovenlypolygon.recipes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.slovenlypolygon.recipes.backend.backendcards.IngredientsGenerator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private IngredientsGenerator generator;
    private Typeface customFont;
    private Button changeViewIngredient;
    private SearchView searchViewIngredient;
    private ScrollView scrollViewIngredient;
    private TextView topTextViewOnToolbarIngredient;
    private LinearLayout allDishesCardHolderIngredient;
    private FloatingActionButton scrollToTopButtonIngredient;

    private void initializeVariablesForIngredient() {
        changeViewIngredient = findViewById(R.id.changeView);
        searchViewIngredient = findViewById(R.id.searchView);
        scrollViewIngredient = findViewById(R.id.scrollView);
        scrollToTopButtonIngredient = findViewById(R.id.floatingActionButton);
        allDishesCardHolderIngredient = findViewById(R.id.allDishesCardHolder);
        topTextViewOnToolbarIngredient = findViewById(R.id.topTextViewOnToolbar);
        searchViewIngredient.setOnClickListener(v -> searchViewIngredient.setIconified(false));

        customFont = Typeface.createFromAsset(getAssets(), "fonts/17651.ttf");
        topTextViewOnToolbarIngredient.setTypeface(customFont);
        changeViewIngredient.setTypeface(customFont);
        scrollToTopButtonIngredient.hide();

        generator = new IngredientsGenerator(LayoutInflater.from(this));
    }

    private void generateCardsWithIngredients() {
        try {
            generator.setIngredientURLMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.urls)));
            generator.setDirtyToCleanedMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));
            generator.setCustomFont(customFont);
            generator.setContext(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CardView cardView : generator.generateIngredients(allDishesCardHolderIngredient)) {
            allDishesCardHolderIngredient.addView(cardView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_ingredients);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForIngredient();
        generateCardsWithIngredients();

        changeViewIngredient.setOnClickListener(t -> {
            if (IngredientsGenerator.checkedCards.containsValue(true)) {
                goToRecipes();
            } else {
                Toast.makeText(this, R.string.none_selected, Toast.LENGTH_SHORT).show();
            }
        });

        scrollToTopButtonIngredient.setOnClickListener(t -> scrollViewIngredient.smoothScrollTo(0, 0));
        scrollViewIngredient.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (oldScrollY - scrollY < 0 && scrollY > 2000) {
                scrollToTopButtonIngredient.show();
            } else {
                scrollToTopButtonIngredient.hide();
            }
        });
    }

    private void goToRecipes() {
        this.startActivity(new Intent(this, RecipesActivity.class));
    }
}