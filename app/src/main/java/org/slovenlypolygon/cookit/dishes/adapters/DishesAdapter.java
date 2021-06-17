package org.slovenlypolygon.cookit.dishes.adapters;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.cookit.R;
import org.slovenlypolygon.cookit.components.entitys.Component;
import org.slovenlypolygon.cookit.dishes.entitys.Dish;
import org.slovenlypolygon.cookit.dishes.entitys.FrontendDish;
import org.slovenlypolygon.cookit.dishes.stepbystep.StepByStepFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> implements Filterable {
    private final boolean highlight;

    private String accent;

    private List<FrontendDish> dishes = new ArrayList<>();
    private List<FrontendDish> original = new ArrayList<>();

    public DishesAdapter(boolean highlight) {
        this.highlight = highlight;
    }

    public List<FrontendDish> getDishes() {
        return dishes;
    }

    public void setDishes(List<FrontendDish> frontendDishes) {
        this.dishes = frontendDishes;
        notifyDataSetChanged();
    }

    public void clearDataset() {
        dishes.clear();
        original.clear();
        notifyDataSetChanged();
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
        FrontendDish dish = dishes.get(i);

        if (highlight) {
            String selectedText = Joiner.on(", ").join(dish.getSelectedIngredients());
            String text = Joiner.on(", ").join(dish.getRestIngredients());
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
        dishViewHolder.cardView.setOnClickListener(view -> {
            StepByStepFragment stepByStepFragment = new StepByStepFragment(dish);

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
                .error(R.drawable.wifi_error_image)
                .fit()
                .centerCrop()
                .into(dishViewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.load(dish.getImageURL())
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.wifi_error_image)
                                .fit()
                                .centerCrop()
                                .into(dishViewHolder.imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Dish> results = new ArrayList<>();

                if (original == null || original.isEmpty()) original = dishes;
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
                dishes = results.values != null ? (List<FrontendDish>) results.values : original;
                notifyDataSetChanged();
            }
        };
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public void addDishes(List<FrontendDish> constructedDishes) {
        dishes.addAll(constructedDishes);
        notifyDataSetChanged();
    }

    public void removeDish(int position) {
        dishes.remove(position);
        notifyItemRemoved(position);
    }

    public Dish getDish(int position) {
        return dishes.get(position);
    }

    public void addUniqueDishes(List<FrontendDish> frontendDishes) {
        dishes.addAll(frontendDishes);
        dishes = new ArrayList<>(new TreeSet<>(dishes));
        notifyDataSetChanged();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView ingredients;
        private final ImageView imageView;
        private final CardView cardView;

        public DishViewHolder(View itemView) {
            super(itemView);

            ingredients = itemView.findViewById(R.id.dishIngredients);
            imageView = itemView.findViewById(R.id.dishImage);
            cardView = itemView.findViewById(R.id.dishCardView);
            name = itemView.findViewById(R.id.dishName);
        }
    }
}
