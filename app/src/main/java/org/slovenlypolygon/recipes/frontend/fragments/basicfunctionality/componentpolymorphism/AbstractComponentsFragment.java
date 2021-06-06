package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.bridges.FragmentAdapterBridge;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.frontend.adapters.ComponentTabAdapter;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentsAdapter;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.DishesFragment;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractComponentsFragment extends AbstractFragment implements FragmentAdapterBridge {
    protected DishComponentDAO dao;
    protected RecyclerView recyclerView;
    protected Button changeViewComponent;
    protected RecyclerView selectedAsTabs;
    protected ComponentTabAdapter componentTabAdapter;
    protected DishComponentsAdapter dishComponentsAdapter;

    private Set<Integer> componentIDs = new HashSet<>();
    private FloatingActionButton scrollToTop;
    private AlertDialog alertDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag("databaseFragment")).getDishComponentDAO();
    }

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
        recyclerView.setLayoutManager(new GridLayoutManager(activity, getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1));

        changeViewComponent = rootView.findViewById(R.id.changeView);
        changeViewComponent.setVisibility(View.INVISIBLE);

        scrollToTop = rootView.findViewById(R.id.scrollToTop);
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

        changeViewComponent.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
        changeViewComponent.setActivated(!isEmpty);
        changeViewComponent.setEnabled(!isEmpty);
        changeViewComponent.setFocusable(isEmpty);
        changeViewComponent.setElevation(isEmpty ? 0 : 16);
        changeViewComponent.setBackground(AppCompatResources.getDrawable(activity, isEmpty ? R.drawable.to_recipes_btn_disabled : R.drawable.to_recipes_button_enabled_with_mask));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addData();
    }

    protected void addData() {
        if (dao == null) {
            dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag("databaseFragment")).getDishComponentDAO();
        }

        if (dishComponentsAdapter == null || componentTabAdapter == null) {
            dishComponentsAdapter = new DishComponentsAdapter(this);
            dishComponentsAdapter.setActivityAdapterBridge(() -> activity);
            dishComponentsAdapter.setDAO(dao);

            componentTabAdapter = new ComponentTabAdapter();
            componentTabAdapter.setDishComponentsAdapter(dishComponentsAdapter);

            dishComponentsAdapter.setIngredientSelectedAdapter(componentTabAdapter);
            dishComponentsAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            selectedAsTabs.setAdapter(componentTabAdapter);
            recyclerView.setAdapter(dishComponentsAdapter);

            componentsChanged(dishComponentsAdapter.getSelectedIDs()); // pseudo-initializer
        }
    }

    public void clearSelected() {
        String title = getString(R.string.resources);
        String message = getString(R.string.sure_reset_q);
        String accept = getString(R.string.reset_agree);
        String decline = getString(R.string.reset_disagree);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(accept, (dialog, temp) -> {
            sureClearSelected();
            alertDialog = null;
        });
        builder.setNegativeButton(decline, (dialog, temp) -> {
            alertDialog = null;
        });

        builder.setCancelable(true);

        alertDialog = builder.create();

        if (!dishComponentsAdapter.getSelectedIDs().isEmpty()) {
            alertDialog.show();
        } else {
            Toast.makeText(getContext(), R.string.nothing_selected_to_reset, Toast.LENGTH_SHORT).show();
        }

        alertDialog = null;
    }

    private void sureClearSelected() {
        componentIDs.clear();

        dishComponentsAdapter.clearSelected();
        componentTabAdapter.clearSelected();

        dishComponentsAdapter.notifyDataSetChanged();
        componentTabAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (alertDialog != null) alertDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        componentsChanged(dishComponentsAdapter.getSelectedIDs());

        if (alertDialog != null) alertDialog.show();
    }
}
