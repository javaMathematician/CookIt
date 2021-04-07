package org.slovenlypolygon.recipes.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilter;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;
import org.slovenlypolygon.recipes.frontend.adapters.DishesAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DishesFragment extends Fragment {
//    private SearchView searchView;
    private RecyclerView recyclerView;
    private DishesAdapter dishesAdapter;
    private FloatingActionButton scrollToTop;
    private List<DishComponent> selectedComponents;
    private boolean highlightSelected = false;

    public void setSelectedComponents(List<DishComponent> selectedIngredients) {
        this.selectedComponents = selectedIngredients;
    }

    public void setHighlightSelected(boolean highlightSelected) {
        this.highlightSelected = highlightSelected;
    }

    private void initializeVariablesForRecipes(View rootView) {
        recyclerView = rootView.findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        searchView = rootView.findViewById(R.id.searchViewOnDishesList);
//        searchView.setOnClickListener(view -> searchView.setIconified(false));

        scrollToTop = rootView.findViewById(R.id.floatingActionButtonInRecipes);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        recyclerView.setRecyclerListener(holder -> {
            if (holder.getAdapterPosition() > 9) {
                scrollToTop.show();
            } else {
                scrollToTop.hide();
            }
        });

        scrollToTop.hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dishes_fragment, container, false);

        initializeVariablesForRecipes(rootView);

        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
        });

        try {
            DishFilter dishFilter = new DishFilterBuilder()
                    .setAssortment(Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes)))
                    .setDishComponents(selectedComponents)
                    .createDishFilter();

            dishesAdapter = new DishesAdapter(dishFilter.getMatchingList(), highlightSelected);
            recyclerView.setAdapter(dishesAdapter.setSelectedIngredients(Objects.requireNonNull(selectedComponents)));
        } catch (IOException e) {
            e.printStackTrace();
        }

/*        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dishesAdapter.getFilter().filter(newText.toLowerCase().replace("ё", "е"));
                return true;
            }
        });*/

        return rootView;
    }
}
