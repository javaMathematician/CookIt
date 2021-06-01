package org.slovenlypolygon.recipes.frontend.fragments.dishes;

public class RecommendedDishesFragment extends DishesFragment {
    @Override
    protected void initializeDataProvider() {
        provider = facade.getRecommendedDishes();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!initialized) {
            getMatches();
        }
    }
}
