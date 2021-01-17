package org.slovenlypolygon.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
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
    private DishesGenerator generator;
    private ScrollView scrollViewRecipe;
    private LinearLayout allDishesCardHolderRecipe;
    private FloatingActionButton scrollToTopButtonRecipe;

    private void initializeVariablesForRecipes() {
        generator = new DishesGenerator(LayoutInflater.from(this));
        scrollViewRecipe = findViewById(R.id.scrollViewRecipe);
        scrollToTopButtonRecipe = findViewById(R.id.floatingActionButtonInRecipes);
        allDishesCardHolderRecipe = findViewById(R.id.allDishesCardHolderRecipe);
        TextView topTextViewOnToolbarRecipe = findViewById(R.id.topTextViewOnToolbarRecipes);

        scrollToTopButtonRecipe.hide();
        topTextViewOnToolbarRecipe.setText(getResources().getString(R.string.dishes_with));

        try {
            generator.setDirtyToCleanedMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                .filter(Map.Entry::getValue) // отбираем пары, в которых трушные значения (выбранные карточки)
                .map(Map.Entry::getKey) // из пары (Map.Entry) получаем ключ (строку с название ингредиента)
                .collect(Collectors.toList());

        try {
            DishFilterBuilder dishFilterBuilder = new DishFilterBuilder(Deserializer.deserializeDish(getResources().openRawResource(R.raw.all_dishes)));
            dishFilterBuilder.setRecipeIngredients(selected);
            generator.setSelectedIngredients(selected);
            generator.setRecipesList(dishFilterBuilder.getMatchingList());

            Map<Dish, CardView> dishCardPair = generator.generateRecipes(allDishesCardHolderRecipe);

            for (Map.Entry<Dish, CardView> entry : dishCardPair.entrySet()) {
                CardView cardView = entry.getValue();

                cardView.setOnClickListener(t -> {
                    setContentView(R.layout.step_by_step);
                    constrainStepByStep(entry.getKey());
                });

                allDishesCardHolderRecipe.addView(cardView);
            }
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
