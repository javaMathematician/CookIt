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
import java.util.List;
import java.util.function.Consumer;

public class TabComponentAdapter extends RecyclerView.Adapter<TabComponentAdapter.ViewHolder> {
    private final List<Component> components = new ArrayList<>();
    private Consumer<Component> crossCallback;
    private RecyclerView recyclerView;

    @Override
    public int getItemCount() {
        return components.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_ingredients_card, viewGroup, false));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Component component = components.get(i);

        viewHolder.text.setText(component.getName());
        viewHolder.deleteButton.setOnClickListener(v -> crossCallback.accept(component));
    }

    public void removeComponent(Component component) {
        components.remove(component);
        notifyDataSetChanged();
    }

    public void updateComponent(Component component) {
        if (component.isSelected()) {
            addComponent(component);
        } else {
            removeComponent(component);
        }
    }

    public void clearSelected() {
        components.clear();
        notifyDataSetChanged();
    }

    public void addComponent(Component component) {
        components.add(component);
        notifyDataSetChanged();

        if (components.size() > 5) {
            recyclerView.smoothScrollToPosition(components.size() - 1); // ПОТОМУ ЧТО БАГАНЫЙ АНДРОИД
        } else {
            recyclerView.scrollToPosition(components.size() - 1);
        }
    }

    public void setCrossClickedCallback(Consumer<Component> crossClickedCallback) {
        this.crossCallback = crossClickedCallback;
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