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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.fragments.StepByStepFragment;
import org.slovenlypolygon.recipes.frontend.fragments.bridges.ActivityAdapterBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> implements Filterable {
    private final boolean highlight;

    private String accent;
    private List<Dish> dishes;
    private List<Dish> original;
    private Set<Integer> selectedIngredients;
    private ActivityAdapterBridge activityAdapterBridge;

    public DishesAdapter(List<Dish> dishes, boolean highlight) {
        this.dishes = dishes;
        this.highlight = highlight;
    }

    public void setActivityAdapterBridge(ActivityAdapterBridge activityAdapterBridge) {
        this.activityAdapterBridge = activityAdapterBridge;
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
        Dish dish = activityAdapterBridge.getActivity().getDaoFacade().getRichDish(dishes.get(i));
        Set<Integer> cleanedDish = dish.getCleanComponents().stream().map(Component::getId).collect(Collectors.toSet());
        Set<Integer> intersection = Sets.intersection(cleanedDish, selectedIngredients);

        if (highlight) {
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
            dishViewHolder.ingredients.setText(Joiner.on(", ").join(dish.getCleanComponents()).toLowerCase());
        }

        dishViewHolder.name.setText(dish.getName());
        dishViewHolder.itemView.setOnClickListener(view -> {
            StepByStepFragment stepByStepFragment = new StepByStepFragment();
            stepByStepFragment.setDish(dish);

            ((AppCompatActivity) view.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragmentHolder, stepByStepFragment, "step_by_step")
                    .addToBackStack(null)
                    .commit();
        });

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.ic_error_image)
                .fit()
                .centerCrop()
                .into(dishViewHolder.imageView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private Set<String> namesFromIDs(Set<Integer> ids) {
        return ids.stream()
                .map(t -> activityAdapterBridge.getActivity().getDaoFacade().getCleanComponentNameByID(t).toLowerCase())
                .collect(Collectors.toSet());
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
                    if (original != null && !original.isEmpty()) {
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
                dishes = results.values != null ? (List<Dish>) results.values : new ArrayList<Dish>();
                notifyDataSetChanged();
            }
        };
    }

    public void setAccent(String accent) {
        this.accent = accent;
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
