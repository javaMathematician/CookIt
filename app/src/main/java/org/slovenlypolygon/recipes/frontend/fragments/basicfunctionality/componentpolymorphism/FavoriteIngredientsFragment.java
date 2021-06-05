package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.frontend.adapters.ComponentTabAdapter;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentsAdapter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteIngredientsFragment extends IngredientsFragment {
    @Override
    protected void addData() {
        dao = activity.getDishComponentDAO();

        if (dishComponentsAdapter == null || componentTabAdapter == null) {
            dishComponentsAdapter = new DishComponentsAdapter(this);
            dishComponentsAdapter.setActivityAdapterBridge(() -> activity);

            componentTabAdapter = new ComponentTabAdapter();
            componentTabAdapter.setDishComponentsAdapter(dishComponentsAdapter);

            dishComponentsAdapter.setIngredientSelectedAdapter(componentTabAdapter);
            dishComponentsAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            selectedAsTabs.setAdapter(componentTabAdapter);
            recyclerView.setAdapter(dishComponentsAdapter);

            componentsChanged(dishComponentsAdapter.getSelectedIDs()); // pseudo-initializer

            dao.getFavoriteComponents()
                    .subscribeOn(Schedulers.newThread())
                    .buffer(200, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(components -> {
                        dishComponentsAdapter.addComponents(components);
                        dishComponentsAdapter.notifyDataSetChanged();
                    }, Throwable::printStackTrace);
        }
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final DishComponentDAO dishComponentDAO = activity.getDishComponentDAO();

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
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

                dishComponentDAO.deleteFavorite(dishComponentsAdapter.getComponents().get(position));
                dishComponentsAdapter.removeComponent(dishComponentsAdapter.getComponents().get(position));
                dishComponentsAdapter.notifyDataSetChanged();
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }
}
