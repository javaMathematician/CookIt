package org.slovenlypolygon.recipes.dishes.fragments;

import android.view.View;

public class RecommendedDishesFragment extends DishesFragment {
    @Override
    protected void initializeDataProvider() {
        provider = dao.getRecommendedDishes();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!initialized) {
            getMatches();
        }
    }

    @Override
    protected void initializeVariablesForDishes(View rootView) {
        super.initializeVariablesForDishes(rootView);
        swipeRefreshLayout.setEnabled(true);
    }
}
