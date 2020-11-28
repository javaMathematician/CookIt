package org.slovenlypolygon.recipes;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import org.slovenlypolygon.recipes.databaseutils.Dish;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private int counter = 0;
    private Toolbar toolbar;
    private List<Dish> dishes;
    private SearchView searchView;
    private TextView topTextViewOnToolbar;
    private LinearLayout allDishesCardHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);
        allDishesCardHolder = findViewById(R.id.allDishesCardHolder);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        searchView.
        CardGenerator generator = new CardGenerator(getResources().openRawResource(R.raw.all_dishes));

        try {
            dishes = generator.main();
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnClickListener(v -> searchView.setIconified(false));

        for (int i = 0, dishesSize = dishes.size(); i < dishesSize; i++) {
            allDishesCardHolder.addView(new DishCard(this, dishes.get(i), i));
        }
    }
}