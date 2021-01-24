package org.slovenlypolygon.recipes.backend.adapters;

import android.content.Context;
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
    private Context context;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new IngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_ingridient, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder ingredientViewHolder, int i) {
        Drawable selectedCard = ContextCompat.getDrawable(context, R.drawable.selected_card);
        Drawable regularCard = ContextCompat.getDrawable(context, R.drawable.regular_card);

        ingredientViewHolder.textView.setText(ingredients.get(i).getName());
        Picasso.get()
                .load(ingredients.get(i).getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .resize(200, 200)
                .centerCrop()
                .into(ingredientViewHolder.imageView);

        ingredientViewHolder.cardView.setOnClickListener(t -> {
            CheckBox checkBox = t.findViewById(R.id.checkBoxOnIngredient);
            LinearLayout linearLayout = t.findViewById(R.id.linearLayoutOnIngredient);

            checkBox.setChecked(!checkBox.isChecked());
            linearLayout.setBackground(checkBox.isChecked() ? selectedCard : regularCard);
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textView;
        private ImageView imageView;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.ingredientCard);
            imageView = itemView.findViewById(R.id.imageOnIngredient);
            textView = itemView.findViewById(R.id.textOnIngredient);
        }
    }
}
