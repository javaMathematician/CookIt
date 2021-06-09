package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.ComponentType;
import org.slovenlypolygon.recipes.frontend.adapters.ComponentAdapter;
import org.slovenlypolygon.recipes.frontend.adapters.TabComponentAdapter;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.DishesFragment;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class AbstractComponentsFragment extends AbstractFragment {
    protected DishComponentDAO dao;
    protected RecyclerView recyclerView;
    protected Button changeViewComponent;
    protected RecyclerView selectedAsTabs;
    protected ComponentAdapter componentAdapter;
    protected TabComponentAdapter tabComponentAdapter;
    protected Set<Component> selectedComponents = new HashSet<>();

    private AlertDialog alertDialog;
    private FloatingActionButton scrollToTop;
    private ContextThemeWrapper contextThemeWrapper;

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

        selectedAsTabs.setAdapter(tabComponentAdapter);
        recyclerView.setAdapter(componentAdapter);

        changeViewComponent.setOnClickListener(view -> goToRecipes());

        initializeAdapters();
        addCallbacks();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        tabComponentAdapter.notifyDataSetChanged();
        componentAdapter.notifyDataSetChanged();
        updateButton();
    }

    private void updateButton() {
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

        contextThemeWrapper = new ContextThemeWrapper(getContext(), getActivity()
                .getSharedPreferences("Theme", Context.MODE_PRIVATE).getString("Theme", "Light")
                .equals("Dark") ? R.style.DarkDialog : R.style.LightDialog);

        initializeDatabase();
        addDataSource();
    }

    private void addDataSource() {
        dao.getComponentByType(setDataSource())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(components -> {
                    componentAdapter.addComponents(components);
                    componentAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }

    protected abstract ComponentType setDataSource();

    protected void addCallbacks() {
        tabComponentAdapter.setCrossClickedCallback(component -> {
            component.setSelected(false);

            tabComponentAdapter.updateComponent(component);
            componentAdapter.updateComponent(component);

            componentsChanged(component);
        });

        componentAdapter.setItemClickCallback(component -> {
            componentAdapter.updateComponent(component);
            tabComponentAdapter.updateComponent(component);

            componentsChanged(component);
        });

        componentAdapter.setLongClickListenerCallback(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                String[] options = {"Добавить в избранное", "Отмена"};

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_dialog, R.id.tv1, options) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ImageView image = view.findViewById(R.id.iv1);

                        options[0] = dao.containsFavorites(component) ? getString(R.string.delete_from_favorites_suggestion) : getString(R.string.add_to_favorites_suggestion);
                        image.setBackground(ContextCompat.getDrawable(getContext(), position == 0 ? R.drawable.to_favorites_icon : R.drawable.cancel_close_clear_icon));

                        return view;
                    }
                };

                AlertDialog alertDialog = new AlertDialog.Builder(contextThemeWrapper)
                        .setTitle(R.string.ingredient_actions)
                        .setAdapter(arrayAdapter, (dialog1, which) -> {
                        }).create();

                alertDialog.show();

                AdapterView.OnItemClickListener onItemClickListener = (parent, view1, position, id) -> {
                    if (position == 0 && dao.containsFavorites(component)) {
                        dao.removeFromFavorites(component);
                        Toast.makeText(getContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();
                        onFavoriteComponentDeleted(component);
                    } else if (position == 0 && !dao.containsFavorites(component)) {
                        dao.addToFavorites(component);
                        Toast.makeText(getContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();
                };

                alertDialog.getListView().setOnItemClickListener(onItemClickListener);
            }
        });
    }

    protected void onFavoriteComponentDeleted(Component component) {
    }

    protected void initializeDatabase() {
        if (dao == null) {
            dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag("databaseFragment")).getDishComponentDAO();
        }
    }

    private void initializeAdapters() {
        if (componentAdapter == null || tabComponentAdapter == null) {
            componentAdapter = new ComponentAdapter();
            componentAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            tabComponentAdapter = new TabComponentAdapter();
            componentAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            selectedAsTabs.setAdapter(tabComponentAdapter);
            recyclerView.setAdapter(componentAdapter);
        }
    }

    public void clearSelected() {
        String title = getString(R.string.resources);
        String message = getString(R.string.sure_reset_q);
        String accept = getString(R.string.reset_agree);
        String decline = getString(R.string.reset_disagree);

        AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
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

        if (!selectedComponents.isEmpty()) {
            alertDialog.show();
        } else {
            Toast.makeText(getContext(), R.string.nothing_selected_to_reset, Toast.LENGTH_SHORT).show();
        }

        alertDialog = null;
    }

    private void sureClearSelected() {
        selectedComponents.clear();

        componentAdapter.clearSelected();
        tabComponentAdapter.clearSelected();

        componentAdapter.notifyDataSetChanged();
        tabComponentAdapter.notifyDataSetChanged();

        updateButton();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (alertDialog != null) alertDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButton();

        if (alertDialog != null) alertDialog.show();
    }
}
