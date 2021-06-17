package org.slovenlypolygon.cookit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.apache.commons.io.FileUtils;
import org.slovenlypolygon.cookit.backend.DatabaseFragment;
import org.slovenlypolygon.cookit.billscanner.BillScanFragment;
import org.slovenlypolygon.cookit.components.categories.CategoriesFragment;
import org.slovenlypolygon.cookit.components.ingredients.FavoriteIngredientsFragment;
import org.slovenlypolygon.cookit.components.ingredients.IngredientsFragment;
import org.slovenlypolygon.cookit.dishes.fragments.DishesFragment;
import org.slovenlypolygon.cookit.dishes.fragments.FavoriteDishesFragment;
import org.slovenlypolygon.cookit.dishes.fragments.RecommendedDishesFragment;
import org.slovenlypolygon.cookit.settings.SettingsFragment;
import org.slovenlypolygon.cookit.shoppinglists.ShoppingListsFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final static String THEME = "Theme";
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private String currentTheme;
    private SearchView searchView;
    private boolean downloadQ;

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notifySharedPreferencesChanged();
        initializeDatabaseAndSettingsFragment();

        setTheme();
        setContentView(R.layout.carcass);
        setFrontend();

        showBaseFragment();
    }

    private void setTheme() {
        sharedPreferences = getSharedPreferences(THEME, Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getString(THEME, "Light");
        setTheme(Objects.equals(currentTheme, "Dark") ? R.style.Dark : R.style.Light);
    }

    private void initializeDatabaseAndSettingsFragment() {
        DatabaseFragment fragment = findOrGetFragment(getString(R.string.backend_database_fragment_tag), DatabaseFragment.class);

        if (!fragment.isAdded()) getSupportFragmentManager().beginTransaction().add(fragment, getString(R.string.backend_database_fragment_tag)).commitNow();
    }

    private void showBaseFragment() {
        Map<String, Integer> map = new HashMap<>();

        map.put(getPackageName() + ".ALL_DISHES", R.id.toDishes);
        map.put(getPackageName() + ".REFRIGERATOR", R.id.toFavoritesIngredients);
        map.put(getPackageName() + ".FAVORITE_DISHES", R.id.toFavoritesDishes);
        map.put(getPackageName() + ".SHOPPING_LISTS", R.id.toShoppingLists);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(getIntent().getAction())) {
                menuItemsActions(entry.getValue());
                break;
            }
        } // заменяет миллион элифов и делает псевдо свитч-кейз

        if (!map.containsKey(getIntent().getAction()) && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            menuItemsActions(R.id.toIngredients);
        }
    }

    private void setFrontend() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(null);
        toolbar.setElevation(0);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            ImageButton themeButton = findViewById(R.id.themeButton);

            if (themeButton != null) {
                themeButton.setBackgroundResource(Objects.equals(sharedPreferences.getString(THEME, "Light"), "Dark") ? R.drawable.dark_mode : R.drawable.light_mode);
                themeButton.setOnClickListener(item -> changeTheme());
            }
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

        searchView = findViewById(R.id.searchView);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    private void menuItemsActions(int id) {
        drawerLayout.closeDrawer(GravityCompat.START);

        String ingredients = "ingredients";
        String categories = "categories";

        if (id == R.id.toIngredients) {
            changeFragment(findOrGetFragment(ingredients, IngredientsFragment.class), ingredients);
        } else if (id == R.id.toDishes) {
            changeFragment(new DishesFragment(), "all_dishes");
        } else if (id == R.id.toCategories) {
            changeFragment(findOrGetFragment(categories, CategoriesFragment.class), categories);
        } else if (id == R.id.toFavoritesDishes) {
            String favorites = getString(R.string.backend_favorite_dishes_fragment_tag);
            changeFragment(findOrGetFragment(favorites, FavoriteDishesFragment.class), favorites);
        } else if (id == R.id.toFavoritesIngredients) {
            String favorites = getString(R.string.backend_favorite_ingredients_fragment_tag);
            changeFragment(findOrGetFragment(favorites, FavoriteIngredientsFragment.class), favorites);
        } else if (id == R.id.toRecommendations) {
            String recommended = getString(R.string.backend_recommended_dishes_fragment_tag);
            changeFragment(findOrGetFragment(recommended, RecommendedDishesFragment.class), recommended);
        } else if (id == R.id.toShoppingLists) {
            String shopping_list = getString(R.string.backend_shopping_list_fragment_tag);
            changeFragment(findOrGetFragment(shopping_list, ShoppingListsFragment.class), shopping_list);
        } else if (id == R.id.scanBill) {
            String billScan = getString(R.string.backend_bill_scan_fragment_tag);
            changeFragment(findOrGetFragment(billScan, BillScanFragment.class), billScan);
        } else if (id == R.id.toSettings) {
            String settings = getString(R.string.backend_settings_fragment_tag);
            changeFragment(findOrGetFragment(settings, SettingsFragment.class), settings);
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

    public void notifySharedPreferencesChanged() {
        downloadQ = getSharedPreferences("org.slovenlypolygon.cookit_preferences", Context.MODE_PRIVATE).getBoolean("download_pictures", false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!downloadQ) {
            try {
                FileUtils.deleteDirectory(getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}