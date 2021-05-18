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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.backend.DataBaseHelper;
import org.slovenlypolygon.recipes.backend.dao.DAOFacade;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.frontend.fragments.ComponentsFragment;
import org.slovenlypolygon.recipes.frontend.fragments.dialogs.RestartAppForThemeQDialog;
import org.slovenlypolygon.recipes.frontend.fragments.dialogs.SureClearSelectedQDialog;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final static String THEME = "Theme";

    private ComponentsFragment componentsFragment;
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private DAOFacade daoFacade;

    public DAOFacade getDaoFacade() {
        return daoFacade;
    }

    void initializeDBAndDAO() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        dataBaseHelper.createDataBase();

        daoFacade = new DAOFacade(dataBaseHelper.openDataBase());
    }

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(THEME, Context.MODE_PRIVATE);

        setTheme(Objects.equals(sharedPreferences.getString(THEME, ""), "Dark") ? R.style.Dark : R.style.Light);
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
            themeButton.setBackgroundResource(Objects.equals(sharedPreferences.getString(THEME, ""), "Dark") ? R.drawable.dark_mode : R.drawable.light_mode);
            themeButton.setOnClickListener(item -> {
                new RestartAppForThemeQDialog().show(getSupportFragmentManager(), "restart_q");
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            menuItemsActions(item.getItemId());
            return false;
        });


        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentHolder, new ComponentsFragment(), "ingredients")
                .commit();
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
            sureClearSelected();
            changeComponentView(ComponentType.INGREDIENT);
        } else if (id == R.id.toDishes) {
            sureClearSelected();
            componentsFragment.goToRecipes(false);
        } else if (id == R.id.toCategories) {
            sureClearSelected();
            changeComponentView(ComponentType.CATEGORY);
        }
    }

    private void changeComponentView(ComponentType componentType) {
        componentsFragment.changeDatasetTo(componentType);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out, R.animator.to_right_in, R.animator.to_right_out)
                .replace(R.id.fragmentHolder, componentsFragment, "ingredients")
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
                .putString(THEME, Objects.equals(sharedPreferences.getString(THEME, "Dark"), "Light") ? "Dark" : "Light")
                .apply();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}