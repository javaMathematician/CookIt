package org.slovenlypolygon.recipes.backend.backendcards;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishesGenerator {
    private LayoutInflater inflater;
    private List<Dish> recipesList;
    private Context context;

    public DishesGenerator(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRecipesList(List<Dish> recipesList) {
        this.recipesList = recipesList;
    }

    public Map<Dish, CardView> generateRecipes(ViewGroup root) {
        Map<Dish, CardView> output = new HashMap<>();

        for (Dish dish : recipesList) {
            CardView currentCard = (CardView) inflater.inflate(R.layout.card_recipe, root, false);
            TextView textOnCard = currentCard.findViewById(R.id.textOnCardRecipe);
            ImageView image = currentCard.findViewById(R.id.imageOnCardRecipe);

            Picasso.get()
                    .load(Uri.parse(dish.getImageURL()))
                    .error(R.drawable.sample_dish_for_error)
                    .resize(200, 200)
                    .centerCrop()
                    .into(image);

            textOnCard.setText(dish.getName());
            output.put(dish, currentCard);
        }

        return output;
    }
}
