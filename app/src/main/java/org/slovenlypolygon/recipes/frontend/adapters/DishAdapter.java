package org.slovenlypolygon.recipes.frontend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private final List<Dish> dishes;

    public DishAdapter(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public DishAdapter.DishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DishAdapter.DishViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dish_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DishAdapter.DishViewHolder dishViewHolder, int i) {
        Dish dish = dishes.get(i);
        dishViewHolder.name.setText(dish.getName());
        dishViewHolder.ingredients.setText(Joiner.on(", ").join(dish.getRecipeIngredients()));
        dishViewHolder.itemView.setOnClickListener(view -> {

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
