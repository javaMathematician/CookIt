package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.bridges.FragmentAdapterBridge;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.frontend.adapters.ComponentTabAdapter;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentsAdapter;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.DishesFragment;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractComponentsFragment extends AbstractFragment implements FragmentAdapterBridge {
    protected RecyclerView recyclerView;
    protected RecyclerView selectedAsTabs;
    protected DishComponentDAO dao;
    protected ComponentTabAdapter componentTabAdapter;
    protected DishComponentsAdapter dishComponentsAdapter;
    private Button changeViewComponent;
    private Set<Integer> componentIDs = new HashSet<>();
    private FloatingActionButton scrollToTop;

    @Override
    protected void searchTextChanged(String newText) {
        dishComponentsAdapter.getFilter().filter(newText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);
        setRetainInstance(true);

        selectedAsTabs = rootView.findViewById(R.id.selectedIngredientsRecyclerView);
        selectedAsTabs.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        recyclerView = rootView.findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        changeViewComponent = rootView.findViewById(R.id.changeView);

        scrollToTop = rootView.findViewById(R.id.floatingActionButton);
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
            public void onScrolled(@NonNull RecyclerView recyclerView1, int dx, int dy) {
                if (dy > 0 || ((LinearLayoutManager) recyclerView1.getLayoutManager()).findFirstCompletelyVisibleItemPosition() < 5) {
                    scrollToTop.hide();
                } else if (dy < 0) {
                    scrollToTop.show();
                }
            }
        });

        selectedAsTabs.setAdapter(componentTabAdapter);
        recyclerView.setAdapter(dishComponentsAdapter);

        changeViewComponent.setOnClickListener(view -> goToRecipes());
        changeViewComponent.setVisibility(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? View.INVISIBLE : View.VISIBLE);

        return rootView;
    }

    public void goToRecipes() {
        componentIDs = dishComponentsAdapter.getSelectedIDs();

        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setHighlightSelected(true);
        dishesFragment.setSelectedComponentIDs(componentIDs);

        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                .replace(R.id.fragmentHolder, dishesFragment, "dishes")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void componentsChanged(Set<Integer> selectedIDs) {
        boolean isEmpty = selectedIDs.isEmpty();

        changeViewComponent.setActivated(!isEmpty);
        changeViewComponent.setEnabled(!isEmpty);
        changeViewComponent.setFocusable(isEmpty);
        changeViewComponent.setElevation(isEmpty ? 0 : 16);
        changeViewComponent.setBackground(AppCompatResources.getDrawable(activity, isEmpty ? R.drawable.to_recipes_btn_disabled : R.drawable.to_recipes_button_enabled_with_mask));
    }

    public void clearSelectedComponents() {
        componentIDs.clear();

        dishComponentsAdapter.clearSelected();
        componentTabAdapter.clearSelected();

        dishComponentsAdapter.notifyDataSetChanged();
        componentTabAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addData();
    }

    protected void addData() {
        dao = activity.getDishComponentDAO();

        dishComponentsAdapter = new DishComponentsAdapter(this);
        dishComponentsAdapter.setActivityAdapterBridge(() -> activity);

        componentTabAdapter = new ComponentTabAdapter();
        componentTabAdapter.setDishComponentsAdapter(dishComponentsAdapter);
        dishComponentsAdapter.setIngredientSelectedAdapter(componentTabAdapter);

        selectedAsTabs.setAdapter(componentTabAdapter);
        recyclerView.setAdapter(dishComponentsAdapter);

        componentsChanged(dishComponentsAdapter.getSelectedIDs()); // pseudo-initializer
    }

    ;
}
