package org.slovenlypolygon.recipes.frontend.fragments.dishes;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.backend.dao.DishComponentDAO;

public class FavoriteDishesFragment extends DishesFragment {
    @Override
    protected void initializeDataProvider() {
        provider = facade.getFavoriteDishes();
    }

    @Override
    protected void initializeVariablesForDishes(View rootView) {
        super.initializeVariablesForDishes(rootView);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            private final DishComponentDAO dishComponentDAO = ((MainActivity) getActivity()).getDishComponentDAO();

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            private void delete(int[] reverseSortedPositions) {

                                for (int position : reverseSortedPositions) {
                                    dishComponentDAO.deleteFavoriteDish(dishesAdapter.getDishes().get(position));
                                    dishesAdapter.getDishes().remove(position);
                                    dishesAdapter.notifyItemRemoved(position);
                                }

                                dishesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                delete(reverseSortedPositions);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                delete(reverseSortedPositions);
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!initialized) {
            getMatches();
        }
    }
}
