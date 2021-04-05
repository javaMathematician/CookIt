package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilter;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.backend.mainobjects.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.DishesAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Dishes extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private DishesAdapter dishesAdapter;
    private FloatingActionButton scrollToTop;

    private void initializeVariablesForRecipes() {
        recyclerView = findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchViewOnDishesList);
        searchView.setOnClickListener(view -> searchView.setIconified(false));

        scrollToTop = findViewById(R.id.floatingActionButtonInRecipes);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dishes_list);

        initializeVariablesForRecipes();

        List<Ingredient> selectedIngredients = getIntent().getParcelableArrayListExtra("selectedIngredients");
        List<Category> selectedCategories = getIntent().getParcelableArrayListExtra("selectedCategories");
        boolean highlightSelected = getIntent().getBooleanExtra("highlight", false);

        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        try {
            DishFilter dishFilter = new DishFilterBuilder()
                    .setAssortment(Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes)))
                    .setRecipeIngredients(selectedIngredients)
                    .setCategories(selectedCategories)
                    .createDishFilter();

            dishesAdapter = new DishesAdapter(dishFilter.getMatchingList(), highlightSelected);
            recyclerView.setAdapter(selectedIngredients != null ?
                    dishesAdapter.setSelectedIngredients(Objects.requireNonNull(selectedIngredients)) :
                    dishesAdapter.setSelectedCategories(Objects.requireNonNull(selectedCategories))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dishesAdapter.getFilter().filter(newText.toLowerCase().replace("ё", "е"));
                return true;
            }
        });
    }
}