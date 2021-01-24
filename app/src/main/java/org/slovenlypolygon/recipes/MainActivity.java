package org.slovenlypolygon.recipes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.backend.adapters.IngredientAdapter;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private SearchView searchViewIngredient;
    private FloatingActionButton scrollToTopButtonIngredient;
    private Map<String, String> dirtyToCleanedMapper;
    private Map<String, String> ingredientURLMapper;

    private void initializeVariablesForIngredient() {
        ingredients = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        changeViewIngredient = findViewById(R.id.changeView);
        searchViewIngredient = findViewById(R.id.searchView);

        scrollToTopButtonIngredient = findViewById(R.id.floatingActionButton);
        searchViewIngredient.setOnClickListener(v -> searchViewIngredient.setIconified(false));
        scrollToTopButtonIngredient.hide();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            dirtyToCleanedMapper = Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned));
            ingredientURLMapper = Deserializer.deserializeMap(getResources().openRawResource(R.raw.ingredient_to_image_url));

            for (String ingredientName : new TreeSet<>(dirtyToCleanedMapper.values())) {
                ingredients.add(new Ingredient(ingredientName, ingredientURLMapper.getOrDefault(ingredientName, "")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_ingredients);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForIngredient();

        IngredientAdapter adapter = new IngredientAdapter(ingredients);
        adapter.setContext(this);

        recyclerView.setAdapter(adapter);
        changeViewIngredient.setOnClickListener(t -> {
            if (false) {
                goToRecipes();
            } else {
                Toast.makeText(this, R.string.none_selected, Toast.LENGTH_SHORT).show();
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

    private class FilterIngredientsTask extends AsyncTask<String, Void, List<Ingredient>> {
        protected List<Ingredient> doInBackground(String... newText) {
            return ingredients;
        }
    }
}