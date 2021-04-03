package org.slovenlypolygon.recipes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtil;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.IngredientsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Ingredients extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Dish> dishes;
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private SearchView searchViewIngredient;
    private FloatingActionButton scrollToTop;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private IngredientsAdapter adapter;

    private void initializeVariablesForIngredient() {
        recyclerView = findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout = findViewById(R.id.drawerMain);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_recipe, R.string.open_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);

        drawerLayout.addDrawerListener(toggle);

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        try {
            dishes = Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        changeViewIngredient = findViewById(R.id.changeView);
        searchViewIngredient = findViewById(R.id.searchView);

        searchViewIngredient.setOnClickListener(view -> {
            searchViewIngredient.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            searchViewIngredient.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            searchViewIngredient.setIconified(false);
        });


        scrollToTop = findViewById(R.id.floatingActionButton);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
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
            Set<String> strings = new TreeSet<>();
            Map<String, String> ingredientURLMapper = new Gson().fromJson(IOUtil.toString(getResources().openRawResource(R.raw.ingredient_to_image_url)), new TypeToken<Map<String, String>>() {
            }.getType());

            for (Dish dish : dishes) {
                strings.addAll(dish.getRecipeIngredients());
            }

            for (String ingredientName : strings) {
                String url = ingredientURLMapper.getOrDefault(ingredientName, "https://sun9-60.userapi.com/dylNRBX-QrACucpHbXaBlobPNfd0ihbv37SJkw/MZ9j1ew2xWA.jpg?ava=1");
                ingredients.add(new Ingredient(ingredientName, url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        initializeVariablesForIngredient();

        adapter = new IngredientsAdapter(ingredients);
        recyclerView.setAdapter(adapter);
        changeViewIngredient.setOnClickListener(t -> {
            List<Ingredient> matching = ingredients.stream().filter(Ingredient::isSelected).collect(Collectors.toList());

            if (!matching.isEmpty()) {
                goToRecipesFromIngredients(matching, true);
            } else {
                Toast.makeText(this, R.string.none_selected_ingredient, Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            //if (id == R.id.toDishCategories) {
            //    Set<String> allCategories = dishes.stream().map(Dish::getCategories).flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
            //    System.out.println(allCategories);;
            if (id == R.id.clearSelected) {
                ingredients.stream().forEach(t -> t.setSelected(false));
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(Ingredients.this, "Сброшено!", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.toSettings || id == R.id.receiptScan) {
                Toast.makeText(Ingredients.this, "В разработке", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.toDishes) {
                goToRecipesFromIngredients(ingredients, false);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        searchViewIngredient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.toLowerCase().replace("ё", "е"));
                return true;
            }
        });
    }

    private void goToRecipesFromIngredients(List<Ingredient> selected, boolean highlight) {
        startActivity(new Intent(this, Dishes.class)
                .putParcelableArrayListExtra("selectedIngredients", new ArrayList<>(selected))
                .putExtra("highlight", highlight));
    }
}