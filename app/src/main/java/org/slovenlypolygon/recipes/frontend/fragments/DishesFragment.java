package org.slovenlypolygon.recipes.frontend.fragments;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.dao.DAOFacade;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.adapters.DishesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishesFragment extends AbstractFragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton scrollToTop;
    private Set<Integer> selectedComponents;
    private DishesAdapter dishesAdapter;
    private boolean highlightSelected;

    public void setSelectedComponentIDs(Set<Integer> selectedComponentIDs) {
        this.selectedComponents = selectedComponentIDs;
    }

    public void setHighlightSelected(boolean highlightSelected) {
        this.highlightSelected = highlightSelected;
    }

    private void initializeVariablesForDishes(View rootView) {
        recyclerView = rootView.findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.searchView);
        searchView.setOnClickListener(view -> searchView.setIconified(false));

        scrollToTop = rootView.findViewById(R.id.floatingActionButtonInRecipes);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        scrollToTop.hide();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    scrollToTop.hide();
                } else if (dy < 0) {
                    scrollToTop.show();
                }
            }
        });
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
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        List<Dish> output = new ArrayList<>();
        dishesAdapter = new DishesAdapter(output, highlightSelected);

        dishesAdapter.setAccent(Objects.equals(getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE).getString("Theme", ""), "Dark") ? "#04B97F" : "#BB86FC");
        recyclerView.setAdapter(dishesAdapter);

        DAOFacade daoFacade = ((MainActivity) getActivity()).getDaoFacade();
        Observable<Dish> provider = daoFacade.getDishesFromComponentIDs(selectedComponents);

        provider.subscribeOn(Schedulers.newThread())
                .buffer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(constructedDish -> {
                    output.addAll(constructedDish);
                    dishesAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);

        return rootView;
    }
}
