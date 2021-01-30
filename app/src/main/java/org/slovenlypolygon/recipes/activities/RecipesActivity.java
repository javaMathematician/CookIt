package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilter;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.DishAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RecipesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton scrollToTop;

    private void initializeVariablesForRecipes() {
        recyclerView = findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scrollToTop = findViewById(R.id.floatingActionButtonInRecipes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dishes_list);

        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeVariablesForRecipes();

        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        try {
            ArrayList<Ingredient> selected = getIntent().getParcelableArrayListExtra("selected");
            DishFilter dishFilter = new DishFilterBuilder()
                    .setAssortment(Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes)))
                    .setRecipeIngredients(selected)
                    .createDishFilter();

            DishAdapter adapter = new DishAdapter(dishFilter.getMatchingList());
            adapter.setCleaned(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));

            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constrainStepByStep(Dish dish) {
        ImageView imageView = findViewById(R.id.dishStepByStepImage);

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .resize(1500, 1500)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
    }
}
