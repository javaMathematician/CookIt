package org.slovenlypolygon.recipes.frontend.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.activities.DishActivity;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {
    private final List<Dish> dishes;
    private Map<String, String> cleaned;

    public DishesAdapter(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void setCleaned(Map<String, String> cleaned) {
        this.cleaned = cleaned;
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
        Dish dish = dishes.get(i);

        dishViewHolder.name.setText(dish.getName());
        dishViewHolder.ingredients.setText(Joiner.on(", ").join(dish.getRecipeIngredients().stream().map(t -> cleaned.getOrDefault(t, t)).collect(Collectors.toList())).toLowerCase());

        dishViewHolder.itemView.setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(view.getContext(), DishActivity.class).putExtra("dish", dish).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

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
