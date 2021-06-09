package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Sets;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Component;
import org.slovenlypolygon.recipes.frontend.FrontendDish;
import org.slovenlypolygon.recipes.frontend.adapters.DishesAdapter;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishesFragment extends AbstractFragment {
    protected boolean initialized;
    protected DishComponentDAO dao;
    protected SearchView searchView;
    protected DishesAdapter dishesAdapter;
    protected RecyclerView recyclerView;
    protected Observable<FrontendDish> provider;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton scrollToTop;
    private boolean highlightSelected;
    private Set<Component> selectedComponents = new HashSet<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_frament_tag))).getDishComponentDAO();
    }

    public void setSelectedComponents(Set<Component> selectedComponentIDs) {
        this.selectedComponents = selectedComponentIDs;
    }

    public void setHighlightSelected(boolean highlightSelected) {
        this.highlightSelected = highlightSelected;
    }

    protected void initializeVariablesForDishes(View rootView) {
        recyclerView = rootView.findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1));

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
        setRetainInstance(true);

        initializeVariablesForDishes(rootView);

        if (!initialized) {
            dishesAdapter = new DishesAdapter(highlightSelected);
            dishesAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
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
        provider = dao.getDishesFromComponents(selectedComponents);
    }

    protected void getMatches() {
        dishesAdapter.clearDataset();
        provider.subscribeOn(Schedulers.newThread())
                .map(this::splitIngredients)
                .buffer(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(constructedDish -> {
                    dishesAdapter.addDishes(constructedDish);
                    dishesAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);

        initialized = true;
    }

    private FrontendDish splitIngredients(FrontendDish dish) {
        FrontendDish frontendDish = new FrontendDish(dish);

        Set<Component> components = frontendDish.getCleanComponents();
        frontendDish.setSelectedIngredients(getIngredientNames(Sets.intersection(selectedComponents, components)));
        frontendDish.setRestIngredients(getIngredientNames(Sets.difference(components, selectedComponents)));

        return frontendDish;
    }

    private Set<String> getIngredientNames(Set<Component> components) {
        return components.stream().map(Component::getName).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (searchView == null) {
            searchView = activity.findViewById(R.id.searchView);
            searchView.setOnClickListener(view -> searchView.setIconified(false));
        }

        dishesAdapter.setAccent(activity.getCurrentTheme().equals("Dark") ? "#FFC84D" : "#2787F5");

        if (provider == null) {
            initializeDataProvider();
        }

        if (dishesAdapter.getItemCount() == 0) {
            getMatches();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!initialized) {
            initialized = true;
        }
    }
}
