package org.slovenlypolygon.recipes.frontend.fragments;

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

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.dao.DAOFacade;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentsAdapter;
import org.slovenlypolygon.recipes.frontend.adapters.ComponentSelectedAdapter;
import org.slovenlypolygon.recipes.frontend.fragments.bridges.FragmentAdapterBridge;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComponentsFragment extends AbstractFragment implements FragmentAdapterBridge {
    private boolean initialized;

    private RecyclerView selectIngredients;
    private ComponentSelectedAdapter componentSelectedAdapter;
    private RecyclerView recyclerView;
    private Button changeViewComponent;
    private FloatingActionButton scrollToTop;
    private DishComponentsAdapter dishComponentsAdapter;
    private Set<Integer> componentIDs = new HashSet<>();

    private void initializeVariablesForComponents(View rootView) {
        selectIngredients = rootView.findViewById(R.id.selectedIngredientsRecyclerView);
        selectIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView = rootView.findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        changeViewComponent = rootView.findViewById(R.id.changeView);

        scrollToTop = rootView.findViewById(R.id.floatingActionButton);
        scrollToTop.setOnClickListener(view -> {
            if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 15) {
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
        dishComponentsAdapter.getFilter().filter(newText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);

        initializeVariablesForComponents(rootView);

        selectIngredients.setAdapter(componentSelectedAdapter);
        recyclerView.setAdapter(dishComponentsAdapter);
        changeViewComponent.setOnClickListener(t -> goToRecipes(true));

        if (!initialized) {
            changeDatasetTo(ComponentType.INGREDIENT);
            initialized = true;
        }

        counterChanged(dishComponentsAdapter.getSelectedIDs().size()); // pseudo-initializer
        return rootView;
    }

    public void goToRecipes(boolean highlight) {
        componentIDs = dishComponentsAdapter.getSelectedIDs();

        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setHighlightSelected(highlight);
        dishesFragment.setSelectedComponentIDs(dishComponentsAdapter.getSelectedIDs());

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                .replace(R.id.fragmentHolder, dishesFragment, "dishes")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void counterChanged(int counter) {
        changeViewComponent.setActivated(counter != 0);
        changeViewComponent.setEnabled(counter != 0);
        changeViewComponent.setFocusable(counter == 0);
        changeViewComponent.setElevation(counter == 0 ? 0 : 16);
        changeViewComponent.setBackground(AppCompatResources.getDrawable(getContext(), counter == 0 ? R.drawable.to_recipes_btn_disabled : R.drawable.to_recipes_button_enabled_with_mask));
    }

    public void clearSelectedComponents() {
        componentIDs.clear();
        dishComponentsAdapter.clearSelected();
        componentSelectedAdapter.clearSelected();
        dishComponentsAdapter.notifyDataSetChanged();
        componentSelectedAdapter.notifyDataSetChanged();
    }

    public void changeDatasetTo(ComponentType componentType) {
        DAOFacade dao = ((MainActivity) getActivity()).getDaoFacade();

        componentSelectedAdapter = new ComponentSelectedAdapter();
        dishComponentsAdapter = new DishComponentsAdapter(this);
        dao.getComponentByType(componentType)
                .subscribeOn(Schedulers.newThread())
                .buffer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(components -> {
                    dishComponentsAdapter.addComponents(components);
                    dishComponentsAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);

        componentSelectedAdapter.setDishComponentsAdapter(dishComponentsAdapter);
        dishComponentsAdapter.setIngredientSelectedAdapter(componentSelectedAdapter);

        selectIngredients.setAdapter(componentSelectedAdapter);
        recyclerView.setAdapter(dishComponentsAdapter);
    }
}
