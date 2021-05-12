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
import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.frontend.fragments.FragmentAdapterBridge;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class DishComponentsAdapter extends RecyclerView.Adapter<DishComponentsAdapter.IngredientViewHolder> implements Filterable {
    private List<? extends Component> components = new ArrayList<>();
    private final WeakReference<FragmentAdapterBridge> bridge;
    private Set<Integer> selectedIDs = new HashSet<>();
    private int counter;

    public DishComponentsAdapter(FragmentAdapterBridge fragmentAdapterBridge) {
        this.bridge = new WeakReference<>(fragmentAdapterBridge);
    }

    public void setComponents(List<? extends Component> components) {
        this.components = components;
    }

    public void setSelectedIDs(Set<Integer> selectedIDs) {
        this.selectedIDs = selectedIDs;
    }

    public Set<Integer> getSelectedIDs() {
        return selectedIDs;
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
        Component ingredient = components.get(i);

        ingredientViewHolder.checkBox.setChecked(selectedIDs.contains(ingredient.getId()));
        ingredientViewHolder.layout.setBackground(selectedIDs.contains(ingredient.getId()) ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
        ingredientViewHolder.textView.setText(ingredient.getName());

        ingredientViewHolder.itemView.setOnClickListener(view -> {
            ingredientViewHolder.checkBox.setChecked(!ingredientViewHolder.checkBox.isChecked());
            ingredientViewHolder.layout.setBackground(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);

            if (selectedIDs.contains(ingredient.getId())) {
                selectedIDs.remove(ingredient.getId());
            } else {
                selectedIDs.add(ingredient.getId());
            }

            counter = selectedIDs.size();
            bridge.get().counterChanged(counter);
        });

        Picasso.get()
                .load(ingredient.getImageURL())
                .error(R.drawable.ic_error_image)
                .fit()
                .centerCrop()
                .into(ingredientViewHolder.imageView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        counter = selectedIDs.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Component> results = new ArrayList<>();

                if (constraint != null) {
                    if (components != null && !components.isEmpty()) {
                        for (Component iterate : components) {
                            if (iterate.getImageURL().toLowerCase().replace("ё", "е").contains(constraint.toString())) {
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
                components = (List<Component>) results.values;
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
