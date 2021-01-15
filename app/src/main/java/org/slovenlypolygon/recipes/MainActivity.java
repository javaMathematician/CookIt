package org.slovenlypolygon.recipes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.backend.backendcards.IngredientsGenerator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private List<CardView> cards;
    private Button changeViewIngredient;
    private IngredientsGenerator generator;
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
        scrollToTopButtonIngredient.hide();

        generator = new IngredientsGenerator(LayoutInflater.from(this));
        try {
            generator.setIngredientURLMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.urls)));
            generator.setDirtyToCleanedMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));
            generator.setContext(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cards = generator.generateIngredients(allDishesCardHolderIngredient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_ingredients);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForIngredient();

        for (CardView card : cards) {
            allDishesCardHolderIngredient.addView(card);
        }

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

        searchViewIngredient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FilterIngredientsTask filterIngredientsTask = new FilterIngredientsTask();
                filterIngredientsTask.execute(newText);
                return true;
            }
        });
    }

    private void goToRecipes() {
        this.startActivity(new Intent(this, RecipesActivity.class));
    }

    @SuppressLint("StaticFieldLeak")
    private class FilterIngredientsTask extends AsyncTask<String, Void, List<CardView>> {
        protected List<CardView> doInBackground(String... newText) {
            return cards.stream().filter(t -> ((TextView) t.findViewById(R.id.textOnCard))
                    .getText().toString().toLowerCase().trim()
                    .contains(newText[0].toLowerCase().trim())).collect(Collectors.toList());
        }

        @Override
        protected void onPostExecute(List<CardView> cardViews) {
            allDishesCardHolderIngredient.removeAllViews();

            try {
                this.get().forEach(allDishesCardHolderIngredient::addView);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}