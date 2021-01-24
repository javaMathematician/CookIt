package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.io.IOException;
import java.util.Objects;

public class RecipesActivity extends AppCompatActivity {
    private ScrollView scrollViewRecipe;
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
        setContentView(R.layout.list_of_recipes);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForRecipes();

        try {
            DishFilterBuilder dishFilterBuilder = new DishFilterBuilder(Deserializer.deserializeDish(getResources().openRawResource(R.raw.all_dishes)));
            dishFilterBuilder.getMatchingList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constrainStepByStep(Dish dish) {
        ImageView imageView = findViewById(R.id.dishStepByStepImage);

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .resize(1000, 1000)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
    }
}
