package org.slovenlypolygon.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.slovenlypolygon.recipes.backend.backendcards.DishesGenerator;
import org.slovenlypolygon.recipes.backend.backendcards.IngredientsGenerator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipesActivity extends AppCompatActivity {
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

            for (CardView cardView : generator.generateRecipes(allDishesCardHolderRecipe)) {
                allDishesCardHolderRecipe.addView(cardView);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
