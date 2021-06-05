package org.slovenlypolygon.recipes.frontend.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.StepByStepFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> implements Filterable {
    private final boolean highlight;

    private String accent;
    private List<Dish> dishes = new ArrayList<>();
    private List<Dish> original = new ArrayList<>();
    private Set<Integer> selectedIngredients;
    private DishComponentDAO dao;

    public DishesAdapter(boolean highlight) {
        this.highlight = highlight;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void clearDataset() {
        dishes.clear();
        original.clear();
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void setSelectedIngredients(Set<Integer> selectedIngredients) {
        this.selectedIngredients = selectedIngredients;
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    @NonNull
    public DishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DishViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dish_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder dishViewHolder, int i) {
        Dish dish = dao.getRichDish(dishes.get(i));

        if (highlight) {
            Set<Integer> cleanedDish = dish.getCleanComponents().stream().map(Component::getId).collect(Collectors.toSet());
            Set<Integer> intersection = Sets.intersection(cleanedDish, selectedIngredients);

            String selectedText = Joiner.on(", ").join(namesFromIDs(intersection));
            String text = Joiner.on(", ").join(namesFromIDs(Sets.difference(cleanedDish, intersection)));
            String output;

            if (selectedText.isEmpty()) {
                output = text;
            } else if (text.isEmpty()) {
                output = String.format("<font color=" + accent + ">%s</font>", selectedText);
            } else {
                output = String.format("<font color=" + accent + ">%s</font>, %s", selectedText, text);
            }

            output = output.replace("\n", "");
            dishViewHolder.ingredients.setText(Html.fromHtml(output, Html.FROM_HTML_MODE_LEGACY));
        } else {
            dishViewHolder.ingredients.setText(Joiner.on(", ").join(dish.getCleanComponents().stream().map(Component::getName).sorted().collect(Collectors.toList())).toLowerCase());
        }

        dishViewHolder.name.setText(dish.getName());
        dishViewHolder.itemView.setOnClickListener(view -> {
            StepByStepFragment stepByStepFragment = new StepByStepFragment();
            stepByStepFragment.setDish(dish);

            ((AppCompatActivity) view.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                    .replace(R.id.fragmentHolder, stepByStepFragment, "step_by_step")
                    .addToBackStack(stepByStepFragment.getTag())
                    .commit();
        });

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(dish.getImageURL())
                .placeholder(R.drawable.loading_animation)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(dishViewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(dish.getImageURL())
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.error_image)
                                .fit()
                                .centerCrop()
                                .into(dishViewHolder.imageView);
                    }
                });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private Set<String> namesFromIDs(Set<Integer> ids) {
        return ids.stream()
                .map(t -> dao.getCleanComponentNameByID(t).toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Dish> results = new ArrayList<>();

                if (original.isEmpty()) {
                    original = dishes;
                }

                if (constraint != null) {
                    if (!original.isEmpty()) {
                        for (Dish iterate : original) {
                            String all = iterate.getName().toLowerCase().replace("ё", "е");

                            if (all.contains(constraint.toString())) {
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
                dishes = results.values != null ? (List<Dish>) results.values : original;
                notifyDataSetChanged();
            }
        };
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public void addDishes(List<? extends Dish> constructedDish) {
        dishes.addAll(constructedDish);
    }

    public void setDAO(DishComponentDAO dao) {
        this.dao = dao;
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
