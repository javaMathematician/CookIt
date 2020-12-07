package org.slovenlypolygon.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import org.slovenlypolygon.recipes.backend.backendcards.CardsFromIngredients;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<Dish> dishes;
    private SearchView searchView;
    private TextView topTextViewOnToolbar;
    private LinearLayout allDishesCardHolder;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        allDishesCardHolder = findViewById(R.id.allDishesCardHolder);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);
        searchView.setOnClickListener(v -> searchView.setIconified(false));
        inflater = LayoutInflater.from(this);

        CardsFromIngredients cardsFromIngredients = new CardsFromIngredients(this);
        cardsFromIngredients.setDishList(getResources().openRawResource(R.raw.all_dishes));
        cardsFromIngredients.setPhotoMapper(getResources().openRawResource(R.raw.ingredient_photo_map));

        CardView generated = (CardView) inflater.inflate(R.layout.card, allDishesCardHolder, false);
        allDishesCardHolder.addView(generated);
    }
}