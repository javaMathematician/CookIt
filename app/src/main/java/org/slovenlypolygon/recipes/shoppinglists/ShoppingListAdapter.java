package org.slovenlypolygon.recipes.shoppinglists;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;

import org.slovenlypolygon.recipes.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ListViewHolder> {
    private final List<ShoppingList> shoppingLists = new ArrayList<>();

    public void addLists(List<ShoppingList> lists) {
        shoppingLists.addAll(lists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);

        listViewHolder.name.setText(shoppingList.getDish().getName());
        listViewHolder.content.setText(Joiner.on(",\n").join(shoppingList.getDish().getDirtyIngredients()));
        listViewHolder.expandButton.setVisibility(View.VISIBLE);

        listViewHolder.cardView.setOnClickListener(view -> {

            if (listViewHolder.constraintLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(listViewHolder.cardView, new AutoTransition());
                listViewHolder.constraintLayout.setVisibility(View.VISIBLE);
                listViewHolder.expandButton.setBackgroundResource(R.drawable.expandable_arrow_up);
            } else {
                TransitionManager.beginDelayedTransition(listViewHolder.constraintLayout, new AutoTransition());
                listViewHolder.constraintLayout.setVisibility(View.GONE);
                listViewHolder.expandButton.setBackgroundResource(R.drawable.expandable_arrow_down);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView content;
        private final CardView cardView;
        private final Button expandButton;
        private final ConstraintLayout constraintLayout;

        public ListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.listName);
            content = itemView.findViewById(R.id.listContent);
            cardView = itemView.findViewById(R.id.listCardView);
            expandButton = itemView.findViewById(R.id.expandListButton);
            constraintLayout = itemView.findViewById(R.id.expandableShoppingList);
        }
    }
}
