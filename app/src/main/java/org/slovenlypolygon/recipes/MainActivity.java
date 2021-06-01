package org.slovenlypolygon.recipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.backend.DataBaseHelper;
import org.slovenlypolygon.recipes.backend.dao.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.frontend.fragments.ComponentsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.dialogs.RestartAppForThemeQDialog;
import org.slovenlypolygon.recipes.frontend.fragments.dialogs.SureClearSelectedQDialog;
import org.slovenlypolygon.recipes.frontend.fragments.dishes.DishesFragment;
import org.slovenlypolygon.recipes.frontend.fragments.dishes.FavoriteDishesFragment;
import org.slovenlypolygon.recipes.frontend.fragments.dishes.RecommendedDishesFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final static String THEME = "Theme";

    private ComponentsFragment componentsFragment;
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private DishComponentDAO dishComponentDao;

    public DishComponentDAO getDishComponentDAO() {
        return dishComponentDao;
    }

    void initializeDBAndDAO() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        dataBaseHelper.createDataBase();

        dishComponentDao = new DishComponentDAO(dataBaseHelper.openDataBase());
    }

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(THEME, Context.MODE_PRIVATE);

        setTheme(Objects.equals(sharedPreferences.getString(THEME, "Light"), "Dark") ? R.style.Dark : R.style.Light);
        initializeDBAndDAO();

        setContentView(R.layout.carcass);
        setFrontend();
    }

    private void setFrontend() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(null);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        toggle.syncState();
        toggle.setHomeAsUpIndicator(android.R.drawable.button_onoff_indicator_off);
        toggle.setDrawerIndicatorEnabled(true);

        toolbar.setNavigationOnClickListener(t -> drawerLayout.openDrawer(GravityCompat.START));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigationView.setItemIconTintList(null);

        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            ImageButton themeButton = findViewById(R.id.themeButton);
            themeButton.setBackgroundResource(Objects.equals(sharedPreferences.getString(THEME, "Light"), "Dark") ? R.drawable.dark_mode : R.drawable.light_mode);
            themeButton.setOnClickListener(item -> {
                new RestartAppForThemeQDialog().show(getSupportFragmentManager(), "restart_q");
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            menuItemsActions(item.getItemId());
            return false;
        });

        changeFragment(new ComponentsFragment(), "ingredients");
    }

    public void sureClearSelected() {
        componentsFragment.clearSelectedComponents();
    }

    private void menuItemsActions(int id) {
        drawerLayout.closeDrawer(GravityCompat.START);
        componentsFragment = (ComponentsFragment) getSupportFragmentManager().findFragmentByTag("ingredients");

        if (componentsFragment == null) {
            componentsFragment = new ComponentsFragment();
        }

        if (id == R.id.clearSelected && componentsFragment.isVisible()) {
            new SureClearSelectedQDialog().show(getSupportFragmentManager(), "sure_clear_selected_q");
        } else if (id == R.id.toIngredients) {
            changeComponentView(ComponentType.INGREDIENT);
        } else if (id == R.id.toDishes) {
            changeFragment(new DishesFragment(), "all_dishes");
        } else if (id == R.id.toCategories) {
            changeComponentView(ComponentType.CATEGORY);
        } else if (id == R.id.toFavorites) {
            changeFragment(new FavoriteDishesFragment(), "favorites");
        } else if (id == R.id.toRecommendations) {
            changeFragment(new RecommendedDishesFragment(), "recommended");
        }
    }

    private void changeComponentView(ComponentType componentType) {
        componentsFragment.changeDatasetTo(componentType);
        changeFragment(componentsFragment, "ingredients");
    }

    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                .replace(R.id.fragmentHolder, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void sureChangeThemeAndRestart() {
        sharedPreferences.edit()
                .putString(THEME, Objects.equals(sharedPreferences.getString(THEME, "Light"), "Light") ? "Dark" : "Light")
                .apply();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
