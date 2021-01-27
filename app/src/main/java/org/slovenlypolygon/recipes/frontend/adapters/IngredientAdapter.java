package org.slovenlypolygon.recipes.frontend.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new IngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder ingredientViewHolder, int i) {
        Ingredient ingredient = ingredients.get(i);

        ingredientViewHolder.textView.setText(ingredient.getName());
        ingredientViewHolder.itemView.setOnClickListener(view -> {
            Drawable selectedCard = ContextCompat.getDrawable(view.getContext(), R.drawable.selected_card);
            Drawable regularCard = ContextCompat.getDrawable(view.getContext(), R.drawable.regular_card);

            CardView currentCard = view.findViewById(R.id.ingredientCard);
            CheckBox checkBox = currentCard.findViewById(R.id.checkBoxOnIngredient);
            LinearLayout layout = currentCard.findViewById(R.id.linearLayoutOnIngredient);

            checkBox.setChecked(!checkBox.isChecked());
            layout.setBackground(checkBox.isChecked() ? selectedCard : regularCard);
            currentCard.setBackground(checkBox.isChecked() ? selectedCard : regularCard);
            ingredient.setSelected(!ingredient.isSelected());

            System.out.println(ingredient);
        });

        Picasso.get()
                .load(ingredient.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .resize(200, 200)
                .centerCrop()
                .into(ingredientViewHolder.imageView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageOnIngredient);
            textView = itemView.findViewById(R.id.textOnIngredient);
        }
    }
}
