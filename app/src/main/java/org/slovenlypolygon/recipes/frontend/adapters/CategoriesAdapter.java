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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> implements Filterable {
    private List<Category> categories;
    private List<Category> original;

    public CategoriesAdapter(List<Category> ingredients) {
        this.categories = ingredients;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public CategoriesAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CategoriesAdapter.CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.CategoryViewHolder ingredientViewHolder, int i) {
        Category category = categories.get(i);

        ingredientViewHolder.checkBox.setChecked(category.isSelected());
        ingredientViewHolder.layout.setBackground(category.isSelected() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
        ingredientViewHolder.textView.setText(category.getName());

        ingredientViewHolder.itemView.setOnClickListener(view -> {
            ingredientViewHolder.checkBox.setChecked(!ingredientViewHolder.checkBox.isChecked());
            ingredientViewHolder.layout.setBackground(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
            category.setSelected(!category.isSelected());
        });

/*
        Picasso.get()
                .load(category.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .fit()
                .centerCrop()
                .into(ingredientViewHolder.imageView);
*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Category> results = new ArrayList<>();

                if (original == null) {
                    original = categories;
                }

                if (constraint != null) {
                    if (original != null && original.size() > 0) {
                        for (Category iterate : original) {
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
                categories = (List<Category>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final CheckBox checkBox;
        private final LinearLayout layout;
        private final Drawable regularCard;
        private final Drawable selectedCard;

        public CategoryViewHolder(View itemView) {
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
