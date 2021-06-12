package org.slovenlypolygon.recipes.dishes.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.database.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.dishes.entitys.FrontendDish;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteDishesFragment extends DishesFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initializeDataProvider() {
        provider = dao.getFavoriteDishes();
    }

    @Override
    protected void initializeVariablesForDishes(View rootView) {
        super.initializeVariablesForDishes(rootView);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final DishComponentDAO dishComponentDAO = ((DatabaseFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag)))).getDishComponentDAO();

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

                dishComponentDAO.deleteFavorite(dishesAdapter.getDish(position));
                dishesAdapter.removeDish(position);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMatches();
    }

    @Override
    protected void getMatches() {
        Set<FrontendDish> frontendDishes = new TreeSet<>();

        dishesAdapter.clearDataset();
        provider.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        frontendDishes::add,
                        Throwable::printStackTrace,
                        () -> dishesAdapter.setDishes(new ArrayList<>(frontendDishes))
                );

        initialized = true;
    }
}

