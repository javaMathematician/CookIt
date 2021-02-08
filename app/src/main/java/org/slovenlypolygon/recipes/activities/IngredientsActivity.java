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
import org.slovenlypolygon.recipes.frontend.adapters.IngredientsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class IngredientsActivity extends AppCompatActivity {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private SearchView searchViewIngredient;
    private FloatingActionButton scrollToTop;

    private void initializeVariablesForIngredient() {
        recyclerView = findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        changeViewIngredient = findViewById(R.id.changeView);
        searchViewIngredient = findViewById(R.id.searchView);
        searchViewIngredient.setOnClickListener(view -> searchViewIngredient.setIconified(false));

        scrollToTop = findViewById(R.id.floatingActionButton);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });
        recyclerView.setRecyclerListener(holder -> {
            if (holder.getAdapterPosition() > 9) {
                scrollToTop.show();
            } else {
                scrollToTop.hide();
            }
        });
        scrollToTop.hide();

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

        recyclerView.setAdapter(new IngredientsAdapter(ingredients));
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
                SearchFilter searchFilter = new SearchFilter();

                try {
                    recyclerView.swapAdapter(new IngredientsAdapter(searchFilter.execute(newText).get()), true);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    private void goToRecipes(List<Ingredient> selected) {
        startActivity(new Intent(this, RecipesActivity.class).putParcelableArrayListExtra("selected", new ArrayList<>(selected)));
    }

    private class SearchFilter extends AsyncTask<String, Void, List<Ingredient>> {
        protected List<Ingredient> doInBackground(String... newText) {
            return ingredients.stream().filter(t -> t.getName().toLowerCase().contains(newText[0].toLowerCase().replace("ё", "е"))).collect(Collectors.toList());
        }
    }
}