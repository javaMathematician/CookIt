package org.slovenlypolygon.recipes.components.ingredients;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.components.AbstractComponentsFragment;
import org.slovenlypolygon.recipes.components.entitys.Component;
import org.slovenlypolygon.recipes.components.entitys.ComponentType;

import java.util.Objects;

import javax.annotation.Nonnull;

public class FavoriteIngredientsFragment extends AbstractComponentsFragment {
    @Override
    protected ComponentType setDataSource() {
        return ComponentType.FAVORITE_COMPONENT;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(@Nonnull Canvas canvas, @Nonnull RecyclerView recyclerView, @Nonnull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                int backgroundCornerOffset = 20;

                Rect boundRect;
                View itemView = viewHolder.itemView;
                Drawable icon = ContextCompat.getDrawable(activity, R.drawable.delete_icon);
                ColorDrawable background = new ColorDrawable(Color.RED);

                int iconMargin = (itemView.getHeight() - Objects.requireNonNull(icon).getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + icon.getIntrinsicHeight();

                if (dX != 0) {
                    int iconLeft;
                    int iconRight;

                    if (dX > 0) {
                        iconLeft = itemView.getLeft() + iconMargin - icon.getIntrinsicWidth();
                        iconRight = itemView.getLeft() + iconMargin;
                        boundRect = new Rect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                    } else {
                        iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                        iconRight = itemView.getRight() - iconMargin;
                        boundRect = new Rect(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    }

                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    boundRect = new Rect(0, 0, 0, 0);
                }

                background.setBounds(boundRect);
                background.draw(canvas);
                icon.draw(canvas);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                Component component = componentAdapter.getComponents().get(position);
                component.setSelected(false);
                Toast.makeText(requireContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();

                onFavoriteComponentDeleted(component);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onFavoriteComponentDeleted(Component component) {
        super.onFavoriteComponentDeleted(component);

        dao.deleteFavorite(component);
        componentAdapter.deleteComponent(component);
        tabComponentAdapter.updateComponent(component);
    }
}
