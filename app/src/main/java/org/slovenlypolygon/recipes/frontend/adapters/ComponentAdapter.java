package org.slovenlypolygon.recipes.frontend.adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Component;
import org.slovenlypolygon.recipes.backend.picasso.PicassoBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.IngredientViewHolder> implements Filterable {
    private final PicassoBuilder picassoBuilder = new PicassoBuilder();

    private List<Component> components = new ArrayList<>();
    private List<Component> original;

    private ContextThemeWrapper contextThemeWrapper;

    private Consumer<Component> longClickListenerCallback;
    private Consumer<Component> itemClickedCallback;
    private boolean downloadQ;

    public void setDownloadQ(boolean downloadQ) {
        this.downloadQ = downloadQ;
    }

    public void clearSelected() {
        components.forEach(t -> t.setSelected(false));
        notifyDataSetChanged();
    }

    public void deleteComponent(Component component) {
        int index = components.indexOf(component);

        if (index != -1) {
            components.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void setItemClickCallback(Consumer<Component> callback) {
        itemClickedCallback = callback;
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

    @Override
    public void onBindViewHolder(IngredientViewHolder ingredientViewHolder, int i) {
        Component component = components.get(i);

        ingredientViewHolder.checkBox.setChecked(component.isSelected());
        ingredientViewHolder.layout.setBackground(component.isSelected() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
        ingredientViewHolder.textView.setTextColor(component.isSelected() ? ingredientViewHolder.selectedColor : ingredientViewHolder.regularColor);
        ingredientViewHolder.textView.setText(component.getName());

        ingredientViewHolder.cardView.setOnLongClickListener(view -> {
            longClickListenerCallback.accept(component);
            return false;
        });

        ingredientViewHolder.cardView.setOnClickListener(view -> {
            ingredientViewHolder.checkBox.setChecked(!ingredientViewHolder.checkBox.isChecked());
            ingredientViewHolder.layout.setBackground(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
            ingredientViewHolder.textView.setTextColor(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedColor : ingredientViewHolder.regularColor);

            component.setSelected(!component.isSelected());
            itemClickedCallback.accept(component);
        });

        picassoBuilder
                .setDownloadQ(downloadQ)
                .setImageURL(component.getImageURL())
                .setImageView(ingredientViewHolder.imageView)
                .process();
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
                final List<Component> results = new ArrayList<>();

                if (original == null) original = components;
                if (constraint != null) {
                    if (original != null && !original.isEmpty()) {
                        for (Component iterate : original) {
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
                components = results.values != null ? (List<Component>) results.values : original;
                notifyDataSetChanged();
            }
        };
    }

    public void addComponents(List<? extends Component> components) {
        this.components.addAll(components);
        notifyDataSetChanged();
    }

    public List<Component> getComponents() {
        return components;
    }

    public ContextThemeWrapper getContextThemeWrapper() {
        return contextThemeWrapper;
    }

    public void setContextThemeWrapper(ContextThemeWrapper contextThemeWrapper) {
        this.contextThemeWrapper = contextThemeWrapper;
    }

    public void clearItemLongClickCallback() {
        longClickListenerCallback = component -> {
        };
    }

    public void updateComponent(Component component) {
        int index = components.indexOf(component);
        // выглядит контритуитивно, но мем в том, что ингредиенты равны (.equals), если равны их айдишники.
        // но у них могут быть не равны поля isSelected и т. д.
        // так вот, indexOf найдет индекс по .equals (сравнивая только id), а заменой мы уже изменим компонент (например, на выбраенный)
        components.set(index, component);
        notifyItemChanged(index);
    }

    public void setLongClickListenerCallback(Consumer<Component> longClickListenerCallback) {
        this.longClickListenerCallback = longClickListenerCallback;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final CheckBox checkBox;
        private final LinearLayout layout;
        private final CardView cardView;
        private final Drawable regularCard;
        private final Drawable selectedCard;
        private final int selectedColor;
        private final int regularColor;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textOnIngredient);
            imageView = itemView.findViewById(R.id.imageOnIngredient);
            checkBox = itemView.findViewById(R.id.checkBoxOnIngredient);
            layout = itemView.findViewById(R.id.cardViewBackground);
            cardView = itemView.findViewById(R.id.componentCardView);
            regularCard = ContextCompat.getDrawable(itemView.getContext(), R.drawable.regular_card);
            selectedCard = ContextCompat.getDrawable(itemView.getContext(), R.drawable.selected_card);

            Resources.Theme theme = itemView.getContext().getTheme();
            TypedValue typedValue = new TypedValue();

            theme.resolveAttribute(R.attr.textCardColor, typedValue, true);
            regularColor = typedValue.data;

            theme.resolveAttribute(R.attr.selectedTextCardColor, typedValue, true);
            selectedColor = typedValue.data;
        }
    }
}
