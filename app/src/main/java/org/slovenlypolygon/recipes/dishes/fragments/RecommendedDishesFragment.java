package org.slovenlypolygon.recipes.dishes.fragments;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecommendedDishesFragment extends DishesFragment {
    @Override
    protected void initializeDataProvider() {
        provider = dao.getRecommendedDishes();
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeDataProvider();
        dishesAdapter.clearDataset();

        provider.subscribeOn(Schedulers.newThread())
                .buffer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dishesAdapter::addUniqueDishes, Throwable::printStackTrace);
    }

    @Override
    protected void initializeVariablesForDishes(View rootView) {
        super.initializeVariablesForDishes(rootView);
    }
}
