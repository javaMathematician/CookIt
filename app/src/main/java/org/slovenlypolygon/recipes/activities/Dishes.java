package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilter;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.DishesAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Dishes extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton scrollToTop;

    private void initializeVariablesForRecipes() {
        recyclerView = findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeVariablesForRecipes();

        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        try {
            List<Ingredient> selected = getIntent().getParcelableArrayListExtra("selected");
            DishFilter dishFilter = new DishFilterBuilder()
                    .setAssortment(Deserializer.deserializeDishes(getResources().openRawResource(R.raw.alpha)))
                    .setRecipeIngredients(selected)
                    .createDishFilter();

            recyclerView.setAdapter(new DishesAdapter(dishFilter.getMatchingList()).setSelected(selected));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
