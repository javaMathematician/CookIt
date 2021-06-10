package org.slovenlypolygon.recipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality.BillScanFragment;
import org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality.ShoppingListsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism.AbstractComponentsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism.CategoriesFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism.FavoriteIngredientsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism.IngredientsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.DishesFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.FavoriteDishesFragment;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.dishpolymorphism.RecommendedDishesFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final static String THEME = "Theme";

    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private String currentTheme;

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(THEME, Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getString(THEME, "Light");
        setTheme(Objects.equals(currentTheme, "Dark") ? R.style.Dark : R.style.Light);

        initializeDatabaseFragment();
        setContentView(R.layout.carcass);
        setFrontend();
        showBaseFragment();
    }

    private void initializeDatabaseFragment() {
        DatabaseFragment fragment = findOrGetFragment(getString(R.string.backend_database_frament_tag), DatabaseFragment.class);

        if (!fragment.isAdded()) getSupportFragmentManager().beginTransaction().add(fragment, getString(R.string.backend_database_frament_tag)).commitNow();
    }

    private void showBaseFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) menuItemsActions(R.id.toIngredients);
    }

    private void setFrontend() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(null);
        toolbar.setElevation(0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            ImageButton themeButton = findViewById(R.id.themeButton);
            themeButton.setBackgroundResource(Objects.equals(sharedPreferences.getString(THEME, "Light"), "Dark") ? R.drawable.dark_mode : R.drawable.light_mode);
            themeButton.setOnClickListener(item -> changeTheme());
        });

        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setHomeAsUpIndicator(android.R.drawable.button_onoff_indicator_off);
        toolbar.setNavigationOnClickListener(t -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            menuItemsActions(item.getItemId());
            return false;
        });
    }

    private void menuItemsActions(int id) {
        drawerLayout.closeDrawer(GravityCompat.START);

        String ingredients = "ingredients";
        String categories = "categories";

        if (id == R.id.clearSelected) {
            AbstractComponentsFragment abstractComponentsFragment = findOrGetFragment(ingredients, IngredientsFragment.class);

            if (!abstractComponentsFragment.isVisible()) abstractComponentsFragment = findOrGetFragment(categories, IngredientsFragment.class);
            if (abstractComponentsFragment.isVisible()) abstractComponentsFragment.clearSelected();
        } else if (id == R.id.toIngredients) {
            changeFragment(findOrGetFragment(ingredients, IngredientsFragment.class), ingredients);
        } else if (id == R.id.toDishes) {
            changeFragment(new DishesFragment(), "all_dishes");
        } else if (id == R.id.toCategories) {
            changeFragment(findOrGetFragment(categories, CategoriesFragment.class), categories);
        } else if (id == R.id.toFavoritesDishes) {
            String favorites = "favorite_dishes";
            changeFragment(findOrGetFragment(favorites, FavoriteDishesFragment.class), favorites);
        } else if (id == R.id.toFavoritesIngredients) {
            String favorites = "favorite_ingredients";
            changeFragment(findOrGetFragment(favorites, FavoriteIngredientsFragment.class), favorites);
        } else if (id == R.id.toRecommendations) {
            String recommended = "recommended";
            changeFragment(findOrGetFragment(recommended, RecommendedDishesFragment.class), recommended);
        } else if (id == R.id.toShoppingLists) {
            String shopping_list = "shopping_list";
            changeFragment(findOrGetFragment(shopping_list, ShoppingListsFragment.class), shopping_list);
        } else if (id == R.id.scanBill) {
            String billScan = "bill_scan";
            changeFragment(findOrGetFragment(billScan, BillScanFragment.class), billScan);
        }
    }

    private <T> T findOrGetFragment(String tag, Class<T> fragmentClass) {
        T found = (T) getSupportFragmentManager().findFragmentByTag(tag);

        if (found == null) {
            try {
                return fragmentClass.newInstance();
            } catch (IllegalAccessException | InstantiationException exception) {
                exception.printStackTrace();
            }
        }

        return found;
    }

    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                .replace(R.id.fragmentHolder, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void changeTheme() {
        sharedPreferences.edit().putString(THEME, Objects.equals(currentTheme, "Light") ? "Dark" : "Light").apply();
        recreate();
    }

    public String getCurrentTheme() {
        return currentTheme;
    }
}