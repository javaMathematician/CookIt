package org.slovenlypolygon.recipes.frontend.adapters;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.activities.StepByStep;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {
    private final List<Dish> dishes;
    private Set<String> selected;

    public DishesAdapter(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DishViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dish_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DishViewHolder dishViewHolder, int i) {
        dishes.sort((dish1, dish2) -> {
            Set<String> cleanedDish1 = dish1
                    .getRecipeIngredients()
                    .stream()
                    .collect(Collectors.toSet());

            Set<String> cleanedDish2 = dish2
                    .getRecipeIngredients()
                    .stream()
                    .collect(Collectors.toSet());

            return Sets.intersection(cleanedDish2, selected).size() - Sets.intersection(cleanedDish1, selected).size();
        });

        Dish dish = dishes.get(i);
        Set<String> cleanedDish = dish
                .getRecipeIngredients()
                .stream()
                .collect(Collectors.toSet());

        Set<String> intersection = Sets.intersection(cleanedDish, selected);

        String selectedText = Joiner.on(", ").join(intersection).toLowerCase();
        String text = Joiner.on(", ").join(Sets.difference(cleanedDish, intersection)).toLowerCase();
        String output = intersection.isEmpty() ?
                String.format("%s", text).replace("\n", "") :
                String.format("<font color=#9AFF00>%s</font>, %s", selectedText, text).replace("\n", "");

        dishViewHolder.name.setText(dish.getName());
        dishViewHolder.ingredients.setText(Html.fromHtml(output, Html.FROM_HTML_MODE_LEGACY));
        dishViewHolder.itemView.setOnClickListener(view -> view.getContext().startActivity(new Intent(view.getContext(), StepByStep.class).putExtra("dish", dish).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .resize(200, 200)
                .centerCrop()
                .into(dishViewHolder.imageView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public DishesAdapter setSelected(List<Ingredient> selected) {
        this.selected = selected.stream()
                .map(Ingredient::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return this;
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView ingredients;
        private final ImageView imageView;

        public DishViewHolder(View itemView) {
            super(itemView);

            ingredients = itemView.findViewById(R.id.dishIngredients);
            imageView = itemView.findViewById(R.id.dishImage);
            name = itemView.findViewById(R.id.dishName);
        }
    }
}
