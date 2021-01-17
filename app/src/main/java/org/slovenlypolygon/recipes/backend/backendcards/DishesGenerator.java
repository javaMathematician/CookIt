package org.slovenlypolygon.recipes.backend.backendcards;

import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.common.collect.Sets;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DishesGenerator {
    private List<Dish> recipesList;
    private LayoutInflater inflater;
    private Map<String, String> dirtyToCleanedMapper;
    private List<String> selectedIngredients;

    public DishesGenerator(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setRecipesList(List<Dish> recipesList) {
        this.recipesList = recipesList;
    }

    public Map<Dish, CardView> generateRecipes(ViewGroup root) {
        Map<Dish, CardView> output = new HashMap<>();

        for (Dish dish : recipesList) {
            CardView currentCard = (CardView) inflater.inflate(R.layout.card_dish, root, false);
            TextView name = currentCard.findViewById(R.id.textOnCardRecipe);
            ImageView image = currentCard.findViewById(R.id.imageOnCardRecipe);
            TextView ingredients = currentCard.findViewById(R.id.ingredientsList);

            name.setText(dish.getName());
            Picasso.get()
                    .load(Uri.parse(dish.getImageURL()))
                    .error(R.drawable.sample_dish_for_error)
                    .resize(200, 200)
                    .centerCrop()
                    .into(image);

            Set<String> selectedSet = new HashSet<>(selectedIngredients);
            Set<String> providedSet = dish.getRecipeIngredients().stream().map(t -> dirtyToCleanedMapper.get(t)).collect(Collectors.toSet());

            StringBuilder text = new StringBuilder();
            for (String selected : selectedIngredients) {
                if (providedSet.contains(selected)) {
                    text.append(String.format("<font color=#DDCC00>%s</font>, ", selected));
                }
            }

            for (String rest : Sets.difference(providedSet, selectedSet)) {
                if (rest != null) {
                    text.append(rest).append(", ");
                }
            }

            text.setLength(text.length() - 2);
            ingredients.setText(Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_COMPACT));

            output.put(dish, currentCard);
        }

        return output;
    }

    public Map<String, String> getDirtyToCleanedMapper() {
        return dirtyToCleanedMapper;
    }

    public void setDirtyToCleanedMapper(Map<String, String> dirtyToCleanedMapper) {
        this.dirtyToCleanedMapper = dirtyToCleanedMapper;
    }

    public List<String> getSelectedIngredients() {
        return selectedIngredients;
    }

    public void setSelectedIngredients(List<String> selectedIngredients) {
        this.selectedIngredients = selectedIngredients;
    }
}
