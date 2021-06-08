package org.slovenlypolygon.recipes.frontend.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;

public class ShoppingListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // holder for lists
    public static class ShoppingListsHolder extends RecyclerView.ViewHolder {
        private final CardView listCardView;
        private final TextView listName;
        private final Button toListButton;

        public ShoppingListsHolder(View itemView) {
            super(itemView);

            listCardView = itemView.findViewById(R.id.listCardView);
            listName = itemView.findViewById(R.id.listName);
            toListButton = itemView.findViewById(R.id.toListButton);
        }
    }

    // holder for addListCard
    public static class AddListHolder extends RecyclerView.ViewHolder {
        private final CardView addListCardView;
        private final Button addListButton;

        public AddListHolder(View itemView) {
            super(itemView);

            addListCardView = itemView.findViewById(R.id.addListCardView);
            addListButton = itemView.findViewById(R.id.addListButton);
        }
    }
}
