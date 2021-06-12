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
    private List<ShoppingList> shoppingLists = new ArrayList<>();

    public void addList(List<ShoppingList> lists) {
        int size = shoppingLists.size();

        shoppingLists.addAll(lists);
        notifyItemRangeInserted(size - 1, shoppingLists.size());
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);

        CardView cardView = holder.cardView;

        TextView name = holder.name;
        name.setText(shoppingList.getDish().getName());

        TextView content = holder.content;
        content.setText(Joiner.on(",\n").join(shoppingList.getDish().getDirtyIngredients()));

        Button expandButton = holder.expandButton;
        expandButton.setVisibility(View.VISIBLE);

        ConstraintLayout constraintLayout = holder.constraintLayout;

        cardView.setOnClickListener(view -> {
            if (constraintLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                constraintLayout.setVisibility(View.VISIBLE);
                expandButton.setBackgroundResource(R.drawable.expandable_arrow_up);
            } else {
                constraintLayout.setVisibility(View.GONE);
                expandButton.setBackgroundResource(R.drawable.expandable_arrow_down);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
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
