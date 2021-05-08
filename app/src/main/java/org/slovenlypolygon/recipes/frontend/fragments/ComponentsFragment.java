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
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawComponent;
import org.slovenlypolygon.recipes.backend.utils.FragmentAdapterBridge;
import org.slovenlypolygon.recipes.frontend.adapters.DishComponentsAdapter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ComponentsFragment extends AbstractFragment implements FragmentAdapterBridge {
    private RecyclerView recyclerView;
    private Button changeViewIngredient;
    private FloatingActionButton scrollToTop;
    private DishComponentsAdapter dishComponentsAdapter;
    private final Set<RawComponent> components = new HashSet<>();

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
        recyclerView.addRecyclerListener(holder -> {
            if (holder.getBindingAdapterPosition() > 9) {
                scrollToTop.show();
            } else {
                scrollToTop.hide();
            }
        });
        scrollToTop.hide();

        dishComponentsAdapter = new DishComponentsAdapter(((MainActivity) getActivity()).getDao().getAllCategories(), this);
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
        changeViewIngredient.setOnClickListener(t -> {
        });

        counterChanged(dishComponentsAdapter.getCounter()); // pseudo-initializer
        return rootView;
    }

    public void goToRecipes(boolean highlight) {
        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setHighlightSelected(highlight);

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
        if (counter == 0) {
            changeViewIngredient.setBackground(AppCompatResources.getDrawable(Objects.requireNonNull(getContext()), R.drawable.to_recipes_btn_disabled));
            changeViewIngredient.setActivated(false);
            changeViewIngredient.setEnabled(false);
            changeViewIngredient.setFocusable(true);
            changeViewIngredient.setElevation(0);
        } else {
            changeViewIngredient.setBackground(AppCompatResources.getDrawable(Objects.requireNonNull(getContext()), R.drawable.to_recipes_button_enabled));
            changeViewIngredient.setActivated(true);
            changeViewIngredient.setEnabled(true);
            changeViewIngredient.setFocusable(true);
            changeViewIngredient.setElevation(16);
        }
    }

    public void clearSelectedComponents() {
        components.clear();
    }
}
