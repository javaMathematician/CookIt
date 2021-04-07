/*
package org.slovenlypolygon.recipes.activities.dummy;

import android.content.Intent;
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
import org.slovenlypolygon.recipes.backend.mainobjects.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.CategoriesAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Categories extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Dish> dishes;
    private RecyclerView recyclerView;
    private Button changeViewCategory;
    private CategoriesAdapter adapter;
    private SearchView searchViewCategory;
    private FloatingActionButton scrollToTop;
    private final List<Category> categories = new ArrayList<>();

    private void initializeVariablesForCategory() {
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

        changeViewCategory = findViewById(R.id.changeView);
        searchViewCategory = findViewById(R.id.searchView);

        searchViewCategory.setOnClickListener(view -> {
            searchViewCategory.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            searchViewCategory.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            searchViewCategory.setIconified(false);
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
            Map<String, String> CategoryURLMapper = new Gson().fromJson(IOUtil.toString(getResources().openRawResource(R.raw.category_to_image_url)), new TypeToken<Map<String, String>>() {
            }.getType());

            for (Dish dish : dishes) {
                strings.addAll(dish.getCategories());
            }

            for (String CategoryName : strings) {
                String url = CategoryURLMapper.getOrDefault(CategoryName, "");
                categories.add(new Category(CategoryName, url));
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

        initializeVariablesForCategory();

        adapter = new CategoriesAdapter(categories);
        recyclerView.setAdapter(adapter);
        changeViewCategory.setOnClickListener(t -> {
            List<Category> matching = categories.stream().filter(Category::isSelected).collect(Collectors.toList());

            if (!matching.isEmpty()) {
                goToRecipesFromCategories(matching, true);
            } else {
                Toast.makeText(this, R.string.none_selected_category, Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.clearSelected) {
                categories.stream().forEach(t -> t.setSelected(false));
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(this, "Сброшено!", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.toSettings || id == R.id.receiptScan) {
                Toast.makeText(this, "В разработке", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.toDishes) {
                goToRecipesFromCategories(categories, false);
            } else if (id == R.id.toIngredients) {
                startActivity(new Intent(this, Ingredient.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        searchViewCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void goToRecipesFromCategories(List<Category> selected, boolean highlight) {
        startActivity(new Intent(this, Dishes.class)
                .putParcelableArrayListExtra("selectedCategories", new ArrayList<>(selected))
                .putExtra("highlight", highlight));
    }
}
*/
