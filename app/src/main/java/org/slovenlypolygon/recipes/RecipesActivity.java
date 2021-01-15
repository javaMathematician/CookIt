package org.slovenlypolygon.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.backend.backendcards.DishesGenerator;
import org.slovenlypolygon.recipes.backend.backendcards.IngredientsGenerator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipesActivity extends AppCompatActivity {
    private int savedScrollState;
    private Button changeViewRecipe;
    private DishesGenerator generator;
    private ScrollView scrollViewRecipe;
    private TextView topTextViewOnToolbarRecipe;
    private LinearLayout allDishesCardHolderRecipe;
    private FloatingActionButton scrollToTopButtonRecipe;

    private void initializeVariablesForRecipes() {
        generator = new DishesGenerator(LayoutInflater.from(this));
        changeViewRecipe = findViewById(R.id.changeViewRecipe);
        scrollViewRecipe = findViewById(R.id.scrollViewRecipe);
        scrollToTopButtonRecipe = findViewById(R.id.floatingActionButtonInRecipes);
        allDishesCardHolderRecipe = findViewById(R.id.allDishesCardHolderRecipe);
        topTextViewOnToolbarRecipe = findViewById(R.id.topTextViewOnToolbarRecipes);

        generator.setContext(this);
        scrollToTopButtonRecipe.hide();

        topTextViewOnToolbarRecipe.setText(getResources().getString(R.string.dishes_with));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_recipes);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariablesForRecipes();

        List<String> selected = IngredientsGenerator.checkedCards
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        try {
            DishFilterBuilder dishFilterBuilder = new DishFilterBuilder(Deserializer.deserializeDish(getResources().openRawResource(R.raw.all_dishes)));
            dishFilterBuilder.setRecipeIngredients(selected);
            generator.setRecipesList(dishFilterBuilder.getMatchingList());

            Map<Dish, CardView> dishCardPair = generator.generateRecipes(allDishesCardHolderRecipe);

            for (Map.Entry<Dish, CardView> entry : dishCardPair.entrySet()) {
                CardView cardView = entry.getValue();
                cardView.setOnClickListener(t -> {
                    setContentView(R.layout.step_by_step);
                    constrainStepByStep(entry.getKey(), cardView);
                });

                allDishesCardHolderRecipe.addView(cardView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constrainStepByStep(Dish dish, CardView cardView) {
        TextView text = cardView.findViewById(R.id.textOnCardRecipe);
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
        Intent intent = getIntent();
        this.finish();
        startActivity(intent);
    }
}
