package org.slovenlypolygon.recipes.frontend.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.bridges.ActivityAdapterBridge;
import org.slovenlypolygon.recipes.backend.bridges.FragmentAdapterBridge;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.Component;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class DishComponentsAdapter extends RecyclerView.Adapter<DishComponentsAdapter.IngredientViewHolder> implements Filterable {
    private final WeakReference<FragmentAdapterBridge> bridge;
    private final Set<Integer> selectedIDs = new HashSet<>();
    private List<Component> components = new ArrayList<>();

    private DishComponentDAO dao;
    private List<Component> original;
    private ComponentTabAdapter componentTabAdapter;
    private ActivityAdapterBridge activityAdapterBridge;

    public DishComponentsAdapter(FragmentAdapterBridge fragmentAdapterBridge) {
        this.bridge = new WeakReference<>(fragmentAdapterBridge);
    }

    public Set<Integer> getSelectedIDs() {
        return selectedIDs;
    }

    public void clearSelected() {
        selectedIDs.clear();
        bridge.get().componentsChanged(Collections.emptySet());
    }

    public void setDAO(DishComponentDAO dao) {
        this.dao = dao;
    }

    public void setActivityAdapterBridge(ActivityAdapterBridge activityAdapterBridge) {
        this.activityAdapterBridge = activityAdapterBridge;
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

        ingredientViewHolder.checkBox.setChecked(selectedIDs.contains(component.getId()));
        ingredientViewHolder.layout.setBackground(selectedIDs.contains(component.getId()) ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
        ingredientViewHolder.textView.setTextColor(selectedIDs.contains(component.getId()) ? ingredientViewHolder.selectedColor : ingredientViewHolder.regularColor);
        ingredientViewHolder.textView.setText(component.getName());

        ingredientViewHolder.itemView.setOnLongClickListener(view -> {
            createDialog(ingredientViewHolder.itemView, component);
            return false;
        });
        ingredientViewHolder.itemView.setOnClickListener(view -> {
            ingredientViewHolder.checkBox.setChecked(!ingredientViewHolder.checkBox.isChecked());
            ingredientViewHolder.layout.setBackground(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedCard : ingredientViewHolder.regularCard);
            ingredientViewHolder.textView.setTextColor(ingredientViewHolder.checkBox.isChecked() ? ingredientViewHolder.selectedColor : ingredientViewHolder.regularColor);

            if (selectedIDs.contains(component.getId())) {
                selectedIDs.remove(component.getId());
                componentTabAdapter.removeComponent(component);
            } else {
                selectedIDs.add(component.getId());
                componentTabAdapter.addComponent(component);
            }

            componentTabAdapter.notifyDataSetChanged();
            bridge.get().componentsChanged(selectedIDs);
        });

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(component.getImageURL())
                .placeholder(R.drawable.loading_animation)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(ingredientViewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(component.getImageURL())
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.error_image)
                                .fit()
                                .centerCrop()
                                .into(ingredientViewHolder.imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        System.out.println(component.getName());
                                    }
                                });
                    }
                });
    }

    private void createDialog(View itemView, Component component) {
        String[] options = {"Добавить в избранное", "Отмена"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(itemView.getContext(), R.layout.item_dialog, R.id.tv1, options) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ImageView image = view.findViewById(R.id.iv1);

                options[0] = dao.containsFavorites(component) ? "Удалить из избранного" : "Добавить в избранное";
                image.setBackground(ContextCompat.getDrawable(itemView.getContext(), position == 0 ? R.drawable.to_favorites_icon : R.drawable.cancel_close_clear_icon));

                return view;
            }
        };

        itemView.setOnLongClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(itemView.getContext(), activityAdapterBridge.getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE).getString("Theme", "Light").equals("Dark") ? R.style.DarkDialog : R.style.LightDialog))
                    .setAdapter(arrayAdapter, (dialog1, which) -> {
                    })
                    .create();

            dialog.show();
            dialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
                System.out.println(dao.containsFavorites(component));
                if (position == 0 && dao.containsFavorites(component)) {
                    dao.removeFromFavorites(component);
                    System.out.println("component was removed from favorites");
                } else if (position == 0 && !dao.containsFavorites(component)) {
                    dao.addToFavorites(component);
                    System.out.println("component was added to favorites");
                }

                dialog.hide();
            });

            return true;
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
                final List<Component> results = new ArrayList<>();

                if (original == null) {
                    original = components;
                }

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
    }

    public List<Component> getComponents() {
        return components;
    }

    public void removeComponent(Component component) {
        selectedIDs.remove(component.getId());
        bridge.get().componentsChanged(selectedIDs);
        notifyDataSetChanged();
    }

    public void setIngredientSelectedAdapter(ComponentTabAdapter componentTabAdapter) {
        this.componentTabAdapter = componentTabAdapter;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final CheckBox checkBox;
        private final LinearLayout layout;
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
