package org.slovenlypolygon.recipes.dishes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import org.slovenlypolygon.recipes.dishes.entitys.Dish;
import org.slovenlypolygon.recipes.dishes.entitys.FrontendDish;
import org.slovenlypolygon.recipes.utils.DeleteSubstrate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeleteSubstrate substrate = new DeleteSubstrate(requireActivity(), position -> {
            dao.deleteFavorite(dishesAdapter.getDish(position));
            dishesAdapter.removeDish(position);
        });

        new ItemTouchHelper(substrate.getItemTouchHelperCallback()).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMatches();
    }

    @Override
    protected void getMatches() {
        Set<FrontendDish> frontendDishes = new TreeSet<>((first, second) -> Comparator.comparing((Function<FrontendDish, String>) Dish::getName).compare(first, second));

        dishesAdapter.clearDataset();
        provider.subscribeOn(Schedulers.newThread())
                .buffer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(frontendDishes::addAll, Throwable::printStackTrace, () -> dishesAdapter.setDishes(new ArrayList<>(frontendDishes)));

        initialized = true;
    }
}

