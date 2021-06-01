package org.slovenlypolygon.recipes.frontend.fragments.dishes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.adapters.DishesAdapter;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishesFragment extends AbstractFragment {
    protected boolean initialized;
    protected SearchView searchView;
    protected RecyclerView recyclerView;
    protected DishesAdapter dishesAdapter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Observable<Dish> provider;
    protected DishComponentDAO facade;
    private boolean highlightSelected;
    private FloatingActionButton scrollToTop;
    private Set<Integer> selectedComponents = new HashSet<>();

    public void setSelectedComponentIDs(Set<Integer> selectedComponentIDs) {
        this.selectedComponents = selectedComponentIDs;
    }

    public void setHighlightSelected(boolean highlightSelected) {
        this.highlightSelected = highlightSelected;
    }

    protected void initializeVariablesForDishes(View rootView) {
        facade = ((MainActivity) getActivity()).getDishComponentDAO();

        recyclerView = rootView.findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = getActivity().findViewById(R.id.searchView);
        searchView.setOnClickListener(view -> searchView.setIconified(false));

        scrollToTop = rootView.findViewById(R.id.floatingActionButtonInRecipes);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
            scrollToTop.hide();
        });

        scrollToTop.hide();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() < 5) {
                    scrollToTop.hide();
                } else if (dy < 0) {
                    scrollToTop.show();
                }
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void searchTextChanged(String newText) {
        dishesAdapter.getFilter().filter(newText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dishes_fragment, container, false);

        initializeVariablesForDishes(rootView);

        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        if (!initialized) {
            initialized = true;

            dishesAdapter = new DishesAdapter(highlightSelected);

            dishesAdapter.setAccent(Objects.equals(getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE).getString("Theme", ""), "Dark") ? "#04B97F" : "#2787F5");
            dishesAdapter.setSelectedIngredients(selectedComponents);
            dishesAdapter.setActivityAdapterBridge(() -> (MainActivity) this.getActivity());

            initializeDataProvider();
            getMatches();
        }

        recyclerView.swapAdapter(dishesAdapter, true);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);

            Collections.shuffle(dishesAdapter.getDishes());
            dishesAdapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
        });

        return rootView;
    }

    protected void initializeDataProvider() {
        provider = facade.getDishesFromComponentIDs(selectedComponents);
    }

    protected void getMatches() {
        dishesAdapter.clearDataset();
        provider.subscribeOn(Schedulers.newThread())
                .buffer(750, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(constructedDish -> {
                    dishesAdapter.getDishes().addAll(constructedDish);
                    dishesAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }
}
