package org.slovenlypolygon.recipes;

import android.content.Context;
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

import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality.BillScanFragment;
import org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality.shoppinglists.ShoppingListFragment;
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

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(THEME, Context.MODE_PRIVATE);
        setTheme(Objects.equals(sharedPreferences.getString(THEME, "Light"), "Dark") ? R.style.Dark : R.style.Light);

        try {
            DatabaseFragment fragment = findOrGetFragment("databaseFragment", DatabaseFragment.class);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(fragment, "databaseFragment")
                    .commitNow();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(findOrGetFragment("databaseFragment", DatabaseFragment.class));
            System.out.println(findOrGetFragment("databaseFragment", DatabaseFragment.class));
            System.out.println(findOrGetFragment("databaseFragment", DatabaseFragment.class));
            System.out.println(findOrGetFragment("databaseFragment", DatabaseFragment.class));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.carcass);
        setFrontend();
    }

    private void setFrontend() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(null);
        toolbar.setElevation(0);
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
                changeTheme();
            });
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            try {
                menuItemsActions(item.getItemId());
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    private void menuItemsActions(int id) throws IllegalAccessException, InstantiationException {
        drawerLayout.closeDrawer(GravityCompat.START);

        String ingredients = "ingredients";
        String categories = "categories";

        if (id == R.id.clearSelected) {
            AbstractComponentsFragment abstractComponentsFragment = findOrGetFragment(ingredients, IngredientsFragment.class);

            if (!abstractComponentsFragment.isVisible()) {
                abstractComponentsFragment = findOrGetFragment(categories, IngredientsFragment.class);
            }

            if (abstractComponentsFragment.isVisible()) {
                abstractComponentsFragment.clearSelected();
            }
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
            changeFragment(findOrGetFragment(shopping_list, ShoppingListFragment.class), shopping_list);
        } else if (id == R.id.scan_bill) {
            String billScan = "bill_scan";
            changeFragment(findOrGetFragment(billScan, BillScanFragment.class), billScan);
        }
    }

    private <T> T findOrGetFragment(String tag, Class<T> fragmentClass) throws InstantiationException, IllegalAccessException {
        T found = (T) getSupportFragmentManager().findFragmentByTag(tag);

        if (found == null) {
            return fragmentClass.newInstance();
        }

        return found;
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

    public void changeTheme() {
        sharedPreferences.edit()
                .putString(THEME, Objects.equals(sharedPreferences.getString(THEME, "Light"), "Light") ? "Dark" : "Light")
                .apply();

        recreate();
    }
}