package org.slovenlypolygon.recipes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.frontend.fragments.DishesFragment;
import org.slovenlypolygon.recipes.frontend.fragments.IngredientsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.SureClearQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private IngredientsFragment ingredientsFragment;
    private List<Dish> dishList = new ArrayList<>();

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carcass);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_holder, new IngredientsFragment(), "ingredients")
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            dishList = Deserializer.deserializeDishes(getResources().openRawResource(R.raw.all_dishes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        toolbar.setNavigationIcon(R.drawable.toggle);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(toggle);
        toolbar.setNavigationOnClickListener(t -> drawerLayout.openDrawer(GravityCompat.START));
        toggle.syncState();
        toggle.setHomeAsUpIndicator(android.R.drawable.button_onoff_indicator_off);
        toggle.setDrawerIndicatorEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            ingredientsFragment = (IngredientsFragment) getSupportFragmentManager().findFragmentByTag("ingredients");
            if (ingredientsFragment == null) {
                ingredientsFragment = new IngredientsFragment();
            }

            if (id == R.id.clearSelected) {
                DialogFragment dialog = new SureClearQ();
                dialog.show(getSupportFragmentManager(), "sure_clear_q");
            } else if (id == R.id.toIngredients) {
                Objects.requireNonNull(getSupportFragmentManager())
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_holder, ingredientsFragment, "ingredients")
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.toDishes) {
                DishesFragment dishesFragment = (DishesFragment) getSupportFragmentManager().findFragmentByTag("dishes");

                if (dishesFragment == null) {
                    dishesFragment = new DishesFragment();
                }

                Objects.requireNonNull(getSupportFragmentManager())
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_holder, dishesFragment, "dishes")
                        .addToBackStack(null)
                        .commit();

                dishesFragment.setSelectedComponents(ingredientsFragment.getAllIngredients());
                dishesFragment.changeDataSet();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    public void sureClearSelected() {
        ingredientsFragment.clearSelectedComponents();
    }

    public List<Dish> getDishList() {
        return dishList;
    }
}
