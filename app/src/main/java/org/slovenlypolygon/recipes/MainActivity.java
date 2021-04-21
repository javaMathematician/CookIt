package org.slovenlypolygon.recipes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.fragments.DishComponentsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.SureClearQ;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String THEME = "Dark";
    private static SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private DishComponentsFragment dishComponentsFragment;
    private List<Dish> dishList = new ArrayList<>();
    private Map<String, List<String>> dishToRawIngredients = new HashMap<>();
    private Map<String, String> ingredientURLMapper = new HashMap<>();
    private Map<String, String> categoryURLMapper = new HashMap<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(THEME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString(THEME, "").equals("Dark")) setTheme(R.style.Dark);
        else setTheme(R.style.Light);
        setContentView(R.layout.carcass);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(null);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ImageButton themeBtn = findViewById(R.id.themeBtn);

                if (sharedPreferences.getString(THEME, "").equals("Dark"))
                    themeBtn.setBackgroundResource(R.drawable.dark_mode);
                else themeBtn.setBackgroundResource(R.drawable.light_mode);

                // Установка темы по клику
                themeBtn.setOnClickListener(item -> {
                    if (sharedPreferences.getString(THEME, "").equals("Dark")) {
                        themeBtn.setBackgroundResource(R.drawable.light_mode);
                        editor.putString(THEME, "Light");
                        editor.apply();
                    } else {
                        themeBtn.setBackgroundResource(R.drawable.dark_mode);
                        editor.putString(THEME, "Dark");
                        editor.apply();
                    }
                    changeTheme(drawerLayout);
                });
            }
        });


        toggle.syncState();
        toggle.setHomeAsUpIndicator(android.R.drawable.button_onoff_indicator_off);
        toggle.setDrawerIndicatorEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_holder, new DishComponentsFragment(), "ingredients")
                .addToBackStack(null)
                .commit();


        try (InputStream ingredientsStream = getResources().openRawResource(R.raw.raw_ingredients);
             InputStream dishesStream = getResources().openRawResource(R.raw.all_dishes);
             InputStream ingredientToImage = getResources().openRawResource(R.raw.ingredient_to_image_url);
             InputStream categoryToImage = getResources().openRawResource(R.raw.category_to_image_url)) {
            dishList = Deserializer.deserializeDishes(dishesStream);
            dishToRawIngredients = Deserializer.deserializeDishToRawIngredients(ingredientsStream);
            ingredientURLMapper = Deserializer.deserializeStringToString(ingredientToImage);
            categoryURLMapper = Deserializer.deserializeStringToString(categoryToImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        toolbar.setNavigationOnClickListener(t -> drawerLayout.openDrawer(GravityCompat.START));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            dishComponentsFragment = (DishComponentsFragment) getSupportFragmentManager().findFragmentByTag("ingredients");
            if (dishComponentsFragment == null) {
                dishComponentsFragment = new DishComponentsFragment();
            }

            if (id == R.id.clearSelected) {
                Fragment current = getSupportFragmentManager().findFragmentByTag("ingredients");

                if (current != null && current.isVisible()) {
                    DialogFragment dialog = new SureClearQ();
                    dialog.show(getSupportFragmentManager(), "sure_clear_q");
                }
            } else if (id == R.id.toIngredients) {
                dishComponentsFragment.setShowCategories(false);
                sureClearSelected();
                showIngredientsFragment();
            } else if (id == R.id.toDishes) {
                dishComponentsFragment.goToRecipes(dishComponentsFragment.getAllIngredients(), false);
            } else if (id == R.id.toCategories) {
                dishComponentsFragment.setShowCategories(true);
                sureClearSelected();
                showIngredientsFragment();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    private void showIngredientsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, new Fragment())
                .addToBackStack(null)
                .commit(); // бешеный костыль, но не знаю, как перерендерить фрагмент))))

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_holder, dishComponentsFragment, "ingredients")
                .addToBackStack(null)
                .commit();
    }

    public void sureClearSelected() {
        dishComponentsFragment.clearSelectedComponents();
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public Map<String, List<String>> getDishToRawIngredients() {
        return dishToRawIngredients;
    }

    public Map<String, String> getIngredientURLMapper() {
        return ingredientURLMapper;
    }

    public Map<String, String> getCategoryURLMapper() {
        return categoryURLMapper;
    }

    private void changeTheme(DrawerLayout drawerLayout) {
        if (sharedPreferences.getString(THEME, "Dark").equals("Dark")) {
            setTheme(R.style.Dark);
        } else {
            setTheme(R.style.Light);
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        // вот тут надо перезапустить активность и запустить ее на том моменте где мы остановились(например на выборе блюда)
        recreate();
    }
}
