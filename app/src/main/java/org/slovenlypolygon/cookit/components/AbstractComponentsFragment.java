package org.slovenlypolygon.cookit.components;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slovenlypolygon.cookit.R;
import org.slovenlypolygon.cookit.abstractfragments.AbstractSearchableContentFragment;
import org.slovenlypolygon.cookit.backend.DatabaseFragment;
import org.slovenlypolygon.cookit.backend.DishComponentDAO;
import org.slovenlypolygon.cookit.components.adapters.ComponentAdapter;
import org.slovenlypolygon.cookit.components.adapters.TabComponentAdapter;
import org.slovenlypolygon.cookit.components.entitys.Component;
import org.slovenlypolygon.cookit.components.entitys.ComponentType;
import org.slovenlypolygon.cookit.dishes.fragments.DishesFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class AbstractComponentsFragment extends AbstractSearchableContentFragment {
    protected DishComponentDAO dao;
    protected RecyclerView recyclerView;
    protected Button changeViewComponent;
    protected RecyclerView selectedAsTabs;
    protected ComponentAdapter componentAdapter;
    protected TabComponentAdapter tabComponentAdapter;
    protected Set<Component> selectedComponents = new HashSet<>();

    protected TextView textView;
    private FloatingActionButton scrollToTop;
    private ContextThemeWrapper contextThemeWrapper;
    @Nullable private AlertDialog actionsWithIngredientDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    protected void searchTextChanged(String newText) {
        componentAdapter.getFilter().filter(newText);
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

        textView = rootView.findViewById(R.id.endOfPageIngredientsTextView);
        changeViewComponent = rootView.findViewById(R.id.changeView);
        changeViewComponent.setVisibility(View.INVISIBLE);

        scrollToTop = rootView.findViewById(R.id.scrollToTop);
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
            public void onScrolled(@NonNull RecyclerView recyclerView1, int dx, int dy) {
                if (dy > 0 || ((LinearLayoutManager) Objects.requireNonNull(recyclerView1.getLayoutManager())).findFirstCompletelyVisibleItemPosition() < 5) {
                    scrollToTop.hide();
                } else if (dy < 0) {
                    scrollToTop.show();
                }
            }
        });

        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                changeViewComponent.animate().translationY(changeViewComponent.getBottom() >> 4).setInterpolator(new AccelerateInterpolator(2)).start();
            }

            @Override
            public void onShow() {
                changeViewComponent.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        selectedAsTabs.setAdapter(tabComponentAdapter);
        recyclerView.setAdapter(componentAdapter);

        changeViewComponent.setOnClickListener(view -> goToRecipes());

        initializeAdapters();
        addCallbacks();

        return rootView;
    }

    public void goToRecipes() {
        DishesFragment dishesFragment = new DishesFragment();
        dishesFragment.setHighlightSelected(true);
        dishesFragment.setSelectedComponents(selectedComponents);

        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                .replace(R.id.fragmentHolder, dishesFragment, "dishes")
                .addToBackStack(null)
                .commit();
    }

    public void componentsChanged(Component component) {
        if (component.isSelected()) {
            selectedComponents.add(component);
        } else {
            selectedComponents.remove(component);
        }

        componentAdapter.notifyDataSetChanged();
        updateButton();
    }

    protected void updateButton() {
        boolean isEmpty = selectedComponents.isEmpty();

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

        contextThemeWrapper = new ContextThemeWrapper(requireContext(), activity.getCurrentTheme().equals("Dark") ? R.style.DarkDialog : R.style.LightDialog);
        initializeDatabase();

        updateData();
    }

    protected void updateData() {
        if (componentAdapter.getComponents().isEmpty()) {
            addDataSource();
        }
    }

    protected void addDataSource() {
        List<Component> components = new ArrayList<>(1300);

        dao.getComponentByType(setDataSource())
                .subscribeOn(Schedulers.newThread())
                .buffer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(componentList -> {
                    componentAdapter.addComponents(componentList);
                    components.addAll(componentList);
                }, Throwable::printStackTrace, () -> { // костыль (на медленных телефонах не успевает подгружаться что-то, так что на всякий случай)
                    if (!components.isEmpty()) {
                        componentAdapter.setComponents(components);
                    }

                    checkQuantity();
                });
    }

    protected void checkQuantity() {
    }

    protected abstract ComponentType setDataSource();

    protected void addCallbacks() {
        tabComponentAdapter.setCrossClickedCallback(component -> {
            component.setSelected(false);

            tabComponentAdapter.updateComponent(component);
            componentAdapter.updateComponent(component);

            componentsChanged(component);
        });

        tabComponentAdapter.setCardClickCallback(componentAdapter::scrollTo);

        componentAdapter.setItemClickCallback(component -> {
            componentAdapter.updateComponent(component);
            tabComponentAdapter.updateComponent(component);

            componentsChanged(component);
        });

        componentAdapter.setLongClickListenerCallback(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                String[] options = {getString(R.string.add_to_favorites_suggestion), "Отмена"};

                final boolean containsFavorites = dao.containsFavorites(component);
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

                actionsWithIngredientDialog = new AlertDialog.Builder(contextThemeWrapper).setTitle(R.string.ingredient_actions).setAdapter(arrayAdapter, (dialog1, which) -> {}).create();
                actionsWithIngredientDialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
                    if (position == 0 && containsFavorites) {
                        dao.removeFromFavorites(component);
                        Toast.makeText(requireContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();
                        onFavoriteComponentDeleted(component);
                    } else if (position == 0) {
                        dao.addToFavorites(component);
                        Toast.makeText(requireContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                    }

                    actionsWithIngredientDialog.dismiss();
                    actionsWithIngredientDialog = null;
                });

                actionsWithIngredientDialog.setOnCancelListener(dialog -> actionsWithIngredientDialog = null);
                actionsWithIngredientDialog.show();
            }
        });
    }

    protected void onFavoriteComponentDeleted(Component component) {
    }

    protected void initializeDatabase() {
        if (dao == null) {
            dao = ((DatabaseFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag)))).getDishComponentDAO();
        }
    }

    private void initializeAdapters() {
        if (componentAdapter == null || tabComponentAdapter == null) {
            componentAdapter = new ComponentAdapter();
            componentAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

            tabComponentAdapter = new TabComponentAdapter();
            componentAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

            selectedAsTabs.setAdapter(tabComponentAdapter);
            recyclerView.setAdapter(componentAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (actionsWithIngredientDialog != null) {
            actionsWithIngredientDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButton();

        if (actionsWithIngredientDialog != null) {
            actionsWithIngredientDialog.show();
        }

        componentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }
}
