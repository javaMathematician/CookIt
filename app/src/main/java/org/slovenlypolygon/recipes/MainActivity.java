package org.slovenlypolygon.recipes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import org.slovenlypolygon.recipes.backend.backendcards.CardsFromIngredients;
import org.slovenlypolygon.recipes.backend.backendcards.CreateCards;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<Dish> dishes;
    private SearchView searchView;
    private TextView topTextViewOnToolbar;
    private Button changeView;
    private LinearLayout allDishesCardHolder;
    private LayoutInflater inflater;
    private CreateCards createCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        allDishesCardHolder = findViewById(R.id.allDishesCardHolder);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);
        changeView = findViewById(R.id.changeView);
        searchView.setOnClickListener(v -> searchView.setIconified(false));
        inflater = LayoutInflater.from(this);

        CardsFromIngredients cardsFromIngredients = new CardsFromIngredients(this);
        cardsFromIngredients.setDishList(getResources().openRawResource(R.raw.all_dishes));
        cardsFromIngredients.setPhotoMapper(getResources().openRawResource(R.raw.ingredient_photo_map));

        Map<String, String> ingredients = new TreeMap<>();

        try {
            ingredients = cardsFromIngredients.getIngredientsMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/17651.ttf");
        topTextViewOnToolbar.setTypeface(customFont);
        changeView.setTypeface(customFont);

        createCards = new CreateCards(ingredients, allDishesCardHolder, customFont, inflater);
    }
}