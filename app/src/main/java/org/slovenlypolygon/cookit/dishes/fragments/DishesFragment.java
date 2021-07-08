package org.slovenlypolygon.cookit.dishes.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Sets;

import org.slovenlypolygon.cookit.MainActivity;
import org.slovenlypolygon.cookit.R;
import org.slovenlypolygon.cookit.abstractfragments.AbstractSearchableContentFragment;
import org.slovenlypolygon.cookit.backend.DatabaseFragment;
import org.slovenlypolygon.cookit.backend.DishComponentDAO;
import org.slovenlypolygon.cookit.components.entitys.Component;
import org.slovenlypolygon.cookit.dishes.adapters.DishesAdapter;
import org.slovenlypolygon.cookit.dishes.entitys.Dish;
import org.slovenlypolygon.cookit.dishes.entitys.FrontendDish;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishesFragment extends AbstractSearchableContentFragment {
    protected boolean initialized;
    protected DishComponentDAO dao;
    protected RecyclerView recyclerView;
    protected DishesAdapter dishesAdapter;
    protected Observable<FrontendDish> provider;
    private boolean highlightSelected;
    private FloatingActionButton scrollToTop;
    private Set<Component> selectedComponents = new HashSet<>();
    private AlertDialog actionsWithDishDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dao = ((DatabaseFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag)))).getDishComponentDAO();
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
            if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() > 15) {
                recyclerView.scrollToPosition(15);
            }

            recyclerView.smoothScrollToPosition(0);
            scrollToTop.hide();
        });

        scrollToTop.hide();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() < 5) {
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
        setRetainInstance(true);

        initializeVariablesForDishes(rootView);

        if (!initialized) {
            dishesAdapter = new DishesAdapter(highlightSelected);
            dishesAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
            dishesAdapter.setDishLongClick(new Consumer<Dish>() {
                @Override
                public void accept(Dish dish) {
                    String[] options = {getString(R.string.add_to_favorites_suggestion), getString(R.string.dismiss)};

                    final boolean containsFavorites = dao.containsFavorites(dish);
                    if (containsFavorites) {
                        options[0] = getString(R.string.delete_from_favorites_suggestion);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.item_dialog, R.id.dialogTextView, options) {
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            ImageView imageView = view.findViewById(R.id.dialogImageView);
                            imageView.setBackground(ContextCompat.getDrawable(requireContext(), position == 0 ? R.drawable.to_favorites_icon : R.drawable.cancel_close_clear_icon));

                            return view;
                        }
                    };

                    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(requireContext(), ((MainActivity) requireActivity()).getCurrentTheme().equals("Dark") ? R.style.DarkProgressDialog : R.style.LightProgressDialog);
                    actionsWithDishDialog = new AlertDialog.Builder(contextThemeWrapper).setTitle(R.string.ingredient_actions).setAdapter(arrayAdapter, (dialog1, which) -> {}).create();
                    actionsWithDishDialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
                        if (position == 0 && containsFavorites) {
                            dao.removeFromFavorites(dish);
                            Toast.makeText(requireContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();
                        } else if (position == 0) {
                            dao.addToFavorites(dish);
                            Toast.makeText(requireContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                        }

                        actionsWithDishDialog.dismiss();
                        actionsWithDishDialog = null;
                    });

                    actionsWithDishDialog.setOnCancelListener(dialog -> actionsWithDishDialog = null);
                    actionsWithDishDialog.show();
                }
            });
        }

        recyclerView.setAdapter(dishesAdapter);
        return rootView;
    }

    protected void initializeDataProvider() {
        provider = dao.getDishesFromComponents(selectedComponents);
    }

    protected void getMatches() {
        provider.subscribeOn(Schedulers.newThread())
                .map(this::splitIngredients)
                .buffer(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dishesAdapter::addDishes, Throwable::printStackTrace);

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

        dishesAdapter.setAccent(activity.getCurrentTheme().equals("Dark") ? "#FFC84D" : "#EE3D48");

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

        if (!initialized) initialized = true;
        if (actionsWithDishDialog != null) actionsWithDishDialog.show();

        dishesAdapter.notifyDataSetChanged(); // убедиться в том, что контент будет показан
    }

    @Override
    public void onPause() {
        super.onPause();

        if (actionsWithDishDialog != null) actionsWithDishDialog.dismiss();
    }
}
