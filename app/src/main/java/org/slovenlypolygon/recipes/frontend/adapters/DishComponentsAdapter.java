package org.slovenlypolygon.recipes.frontend.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawComponent;
import org.slovenlypolygon.recipes.backend.utils.FragmentAdapterBridge;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DishComponentsAdapter extends RecyclerView.Adapter<DishComponentsAdapter.IngredientViewHolder> implements Filterable {
    private final WeakReference<FragmentAdapterBridge> bridge;
    private List<RawComponent> components;
    private int counter;

    public DishComponentsAdapter(List<RawComponent> components, FragmentAdapterBridge fragmentAdapterBridge) {
        this.components = components;
        this.bridge = new WeakReference<>(fragmentAdapterBridge);
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    @Override
    @NonNull
    public IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new IngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_card, viewGroup, false));
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder ingredientViewHolder, int i) {
        RawComponent ingredient = components.get(i);

        ingredientViewHolder.checkBox.setChecked(ingredient.isSelected());
        ingredientViewHolder.layout.setBackground(ingredient.isSelected() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
        ingredientViewHolder.textView.setText(ingredient.getName());

        ingredientViewHolder.itemView.setOnClickListener(view -> {
            ingredientViewHolder.checkBox.setChecked(!ingredientViewHolder.checkBox.isChecked());
            ingredientViewHolder.layout.setBackground(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);

            ingredient.setSelected(!ingredient.isSelected());
            counter += ingredient.isSelected() ? 1 : -1;
            bridge.get().counterChanged(counter);
        });

        Picasso.get()
                .load(ingredient.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .fit()
                .centerCrop()
                .into(ingredientViewHolder.imageView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        counter = (int) components.parallelStream().filter(DishComponent::isSelected).count();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<DishComponent> results = new ArrayList<>();

                if (constraint != null) {
                    if (components != null && !components.isEmpty()) {
                        for (DishComponent iterate : components) {
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
                components = (List<DishComponent>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final CheckBox checkBox;
        private final LinearLayout layout;
        private final Drawable regularCard;
        private final Drawable selectedCard;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textOnIngredient);
            imageView = itemView.findViewById(R.id.imageOnIngredient);
            checkBox = itemView.findViewById(R.id.checkBoxOnIngredient);
            layout = itemView.findViewById(R.id.linearLayoutOnIngredient);
            regularCard = ContextCompat.getDrawable(itemView.getContext(), R.drawable.regular_card);
            selectedCard = ContextCompat.getDrawable(itemView.getContext(), R.drawable.selected_card);
        }
    }
}
