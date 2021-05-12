package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.dao.DAOFacade;
import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentsAdapter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComponentsFragment extends AbstractFragment implements FragmentAdapterBridge {
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private FloatingActionButton scrollToTop;
    private DishComponentsAdapter dishComponentsAdapter;
    private Set<Integer> componentIDs = new HashSet<>();

    private void initializeVariablesForComponents(View rootView) {
        recyclerView = rootView.findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        changeViewIngredient = rootView.findViewById(R.id.changeView);

        scrollToTop = rootView.findViewById(R.id.floatingActionButton);
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
        dishComponentsAdapter.getFilter().filter(newText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);

        initializeVariablesForComponents(rootView);

        recyclerView.setAdapter(dishComponentsAdapter);
        changeViewIngredient.setOnClickListener(t -> goToRecipes(true));

        changeDatasetTo(ComponentType.INGREDIENT);
        counterChanged(dishComponentsAdapter.getCounter()); // pseudo-initializer
        return rootView;
    }

    public void goToRecipes(boolean highlight) {
        componentIDs = dishComponentsAdapter.getSelectedIDs();

        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setHighlightSelected(highlight);
        dishesFragment.setSelectedComponentIDs(dishComponentsAdapter.getSelectedIDs());

        Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentHolder, dishesFragment, "dishes")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void counterChanged(int counter) {
        changeViewIngredient.setActivated(counter != 0);
        changeViewIngredient.setEnabled(counter != 0);
        changeViewIngredient.setFocusable(counter == 0);
        changeViewIngredient.setElevation(counter == 0 ? 0 : 16);
        changeViewIngredient.setBackground(AppCompatResources.getDrawable(Objects.requireNonNull(getContext()), counter == 0 ? R.drawable.to_recipes_btn_disabled : R.drawable.to_recipes_button_enabled_with_mask));
    }

    public void clearSelectedComponents() {
        componentIDs.clear();
    }

    public void changeDatasetTo(ComponentType componentType) {
        DAOFacade dao = ((MainActivity) getActivity()).getDaoFacade();
        Observable<Component> shownContent = dao.getComponentByType(componentType);

        dishComponentsAdapter = new DishComponentsAdapter(this);
        shownContent.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(200, TimeUnit.MILLISECONDS)
                .subscribe(components -> {
                    dishComponentsAdapter.setComponents(components);
                    dishComponentsAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);

        recyclerView.swapAdapter(dishComponentsAdapter, true);
    }
}
