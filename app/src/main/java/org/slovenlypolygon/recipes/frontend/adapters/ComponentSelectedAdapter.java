package org.slovenlypolygon.recipes.frontend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComponentSelectedAdapter extends RecyclerView.Adapter<ComponentSelectedAdapter.ViewHolder> {
    private final List<Component> components = new ArrayList<>();
    private DishComponentsAdapter dishComponentsAdapter;
    private RecyclerView ownerRecyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.ownerRecyclerView = recyclerView;
    }

    public void clearSelected() {
        components.clear();
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    public void setDishComponentsAdapter(DishComponentsAdapter dishComponentsAdapter) {
        this.dishComponentsAdapter = dishComponentsAdapter;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_ingredients_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Component component = components.get(i);

        viewHolder.text.setText(component.getName());
        viewHolder.deleteButton.setOnClickListener(v -> {
            dishComponentsAdapter.removeComponent(component);
            components.remove(component);
            notifyDataSetChanged();
        });
    }

    public void addComponent(Component component) {
        components.add(component);
        components.sort(Comparator.comparing(Component::getName));
        notifyDataSetChanged();

        if (components.size() > 4) {
            ownerRecyclerView.smoothScrollToPosition(components.size() - 1);
        }
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button deleteButton;
        private final TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textOnSelectedIngredient);
            deleteButton = itemView.<CardView>findViewById(R.id.selectedIngredientCard).findViewById(R.id.buttonOnSelectedIngredient);
        }
    }
}