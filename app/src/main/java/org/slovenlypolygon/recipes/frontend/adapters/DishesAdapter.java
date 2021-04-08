package org.slovenlypolygon.recipes.frontend.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;
import org.slovenlypolygon.recipes.frontend.fragments.StepByStepFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> implements Filterable {
    private final boolean highlight;
    private List<Dish> dishes;
    private List<Dish> original;
    private Set<String> selected;

    public DishesAdapter(List<Dish> dishes, boolean highlight) {
        this.dishes = dishes;
        this.highlight = highlight;
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
            Set<String> cleanedDish1 = new HashSet<>(dish1.getRecipeIngredients());
            Set<String> cleanedDish2 = new HashSet<>(dish2.getRecipeIngredients());

            return Sets.intersection(cleanedDish2, selected).size() - Sets.intersection(cleanedDish1, selected).size();
        });

        Dish dish = dishes.get(i);
        Set<String> cleanedDish = new HashSet<>(dish.getRecipeIngredients());
        Set<String> intersection = Sets.intersection(cleanedDish, selected);

        if (highlight) {
            String selectedText = Joiner.on(", ").join(intersection).toLowerCase();
            String text = Joiner.on(", ").join(Sets.difference(cleanedDish, intersection)).toLowerCase();
            String output;

            if (selectedText.length() == 0) {
                output = text;
            } else if (text.length() == 0) {
                output = String.format("<font color=#9AFF00>%s</font>", selectedText);
            } else {
                output = String.format("<font color=#9AFF00>%s</font>, %s", selectedText, text);
            }

            output = output.replace("\n", "");
            dishViewHolder.ingredients.setText(Html.fromHtml(output, Html.FROM_HTML_MODE_LEGACY));
        } else {
            dishViewHolder.ingredients.setText(Joiner.on(", ").join(intersection).toLowerCase());
        }

        dishViewHolder.name.setText(dish.getName());
        dishViewHolder.itemView.setOnClickListener(view -> {
            StepByStepFragment stepByStepFragment = new StepByStepFragment();
            stepByStepFragment.setDish(dish);

            ((AppCompatActivity) view.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, stepByStepFragment, "step_by_step")
                    .addToBackStack(null)
                    .commit();
        });

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .fit()
                .centerCrop()
                .into(dishViewHolder.imageView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public DishesAdapter setSelectedIngredients(List<DishComponent> selected) {
        this.selected = selected.stream()
                .map(DishComponent::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return this;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Dish> results = new ArrayList<>();

                if (original == null) {
                    original = dishes;
                }

                if (constraint != null) {
                    if (original != null && original.size() > 0) {
                        for (Dish iterate : original) {
                            if (iterate.getName().toLowerCase().replace("ё", "е").contains(constraint.toString())) {
                                results.add(iterate);
                            }
                        }
                    }

                    oReturn.values = results;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dishes = (List<Dish>) results.values;
                notifyDataSetChanged();
            }
        };
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
