package org.slovenlypolygon.recipes;

import android.media.Image;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import org.slovenlypolygon.recipes.backend.backendcards.CardsFromIngredients;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;
import org.slovenlypolygon.recipes.frontend.frontendcards.StructuredCard;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<Dish> dishes;
    private SearchView searchView;
    private TextView topTextViewOnToolbar;
    private LinearLayout allDishesCardHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);
        allDishesCardHolder = findViewById(R.id.allDishesCardHolder);
        searchView.setOnClickListener(v -> searchView.setIconified(false));

        CardsFromIngredients cardsFromIngredients = new CardsFromIngredients(this);
        cardsFromIngredients.setDishList(getResources().openRawResource(R.raw.all_dishes));
        cardsFromIngredients.setPhotoMapper(getResources().openRawResource(R.raw.ingredient_photo_map));

        try {
            List<StructuredCard> cards = cardsFromIngredients.getCards();

            for (StructuredCard card : cards) {
                allDishesCardHolder.addView(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}