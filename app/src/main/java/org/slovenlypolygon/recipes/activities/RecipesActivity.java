package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;
import org.slovenlypolygon.recipes.frontend.adapters.DishAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RecipesActivity extends AppCompatActivity {
    private ScrollView scrollViewRecipe;
    private RecyclerView recyclerView;
    private LinearLayout allDishesCardHolderRecipe;
    private FloatingActionButton scrollToTopButtonRecipe;

    private void initializeVariablesForRecipes() {
        scrollViewRecipe = findViewById(R.id.scrollViewRecipe);
        scrollToTopButtonRecipe = findViewById(R.id.floatingActionButtonInRecipes);
        allDishesCardHolderRecipe = findViewById(R.id.allDishesCardHolderRecipe);
        TextView topTextViewOnToolbarRecipe = findViewById(R.id.topTextViewOnToolbarRecipes);

        scrollToTopButtonRecipe.hide();
        topTextViewOnToolbarRecipe.setText(getResources().getString(R.string.dishes_with));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Ingredient> selected = getIntent().getParcelableArrayListExtra("selected");

        setContentView(R.layout.recipes_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForRecipes();

        try {
            DishFilterBuilder dishFilterBuilder = new DishFilterBuilder(Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes)));
            dishFilterBuilder.setRecipeIngredients(selected);

            recyclerView.setAdapter(new DishAdapter(dishFilterBuilder.getMatchingList()));
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
