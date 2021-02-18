package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.adapters.StepByStepAdapter;

import java.util.Objects;

public class DishActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Dish dish;

    private void initializeVariablesForIngredient() {
        dish = getIntent().getParcelableExtra("dish");

        recyclerView = findViewById(R.id.stepByStepRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_by_step);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForIngredient();

        recyclerView.setAdapter(new StepByStepAdapter(dish.getRecipeInstructions()));
    }
}