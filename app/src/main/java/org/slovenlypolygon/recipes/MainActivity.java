package org.slovenlypolygon.recipes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.slovenlypolygon.recipes.backend.backendcards.Generator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Typeface customFont;
    private LayoutInflater inflater;
    private Button changeViewIngredient;
    private SearchView searchViewIngredient;
    private ScrollView scrollViewIngredient;
    private TextView topTextViewOnToolbarIngredient;
    private LinearLayout allDishesCardHolderIngredient;
    private FloatingActionButton scrollToTopButtonIngredient;

    private Button changeViewRecipe;
    private ScrollView scrollViewRecipe;
    private LinearLayout allDishesCardHolderRecipe;
    private FloatingActionButton scrollToTopButtonRecipe;



    private void initializeVariablesForIngredient() {
        inflater = LayoutInflater.from(this);
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
    }

    private void generateCardsWithIngridients() {
        Generator generator = new Generator(inflater);

        try {
            generator.setIngredientURLMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.urls)));
            generator.setDirtyToCleanedMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));
            generator.setCustomFont(customFont);
            generator.setContext(this);
            generator.setRoot(allDishesCardHolderIngredient);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CardView cardView : generator.generate()) {
            allDishesCardHolderIngredient.addView(cardView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_ingredients);

        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForIngredient();
        generateCardsWithIngridients();

        changeViewIngredient.setOnClickListener(t -> {
            if (Generator.checkedCards.containsValue(true)) {
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
        setContentView(R.layout.list_of_recipes);
    }
}