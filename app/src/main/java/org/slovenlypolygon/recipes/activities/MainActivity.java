package org.slovenlypolygon.recipes.activities;

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

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.IngredientAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private SearchView searchViewIngredient;
    private FloatingActionButton scrollToTopButtonIngredient;

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
            Map<String, String> dirtyToCleanedMapper = Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned));
            Map<String, String> ingredientURLMapper = Deserializer.deserializeMap(getResources().openRawResource(R.raw.ingredient_to_image_url));

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
        setContentView(R.layout.ingredients_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForIngredient();

        recyclerView.setAdapter(new IngredientAdapter(ingredients));
        changeViewIngredient.setOnClickListener(t -> {
            List<Ingredient> matching = ingredients.stream().filter(Ingredient::isSelected).collect(Collectors.toList());

            if (!matching.isEmpty()) {
                goToRecipes(matching);
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

                try {
                    recyclerView.swapAdapter(new IngredientAdapter(filterIngredientsTask.execute(newText).get()), true);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    private void goToRecipes(List<Ingredient> selected) {
        Intent intent = new Intent(this, RecipesActivity.class);
        intent.putParcelableArrayListExtra("selected", new ArrayList<>(selected));

        this.startActivity(intent);
    }

    private class FilterIngredientsTask extends AsyncTask<String, Void, List<Ingredient>> {
        protected List<Ingredient> doInBackground(String... newText) {
            return ingredients.stream().filter(t -> {
                String name = t.getName().toLowerCase();
                String request = newText[0].toLowerCase().replace("ё", "е");

                return name.contains(request);
            }).collect(Collectors.toList());
        }
    }
}