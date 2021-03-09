package org.slovenlypolygon.recipes.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.R;

public class Activity extends AppCompatActivity {
    //side menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private void initializeVariablesForActivity() {
        //side menu
        drawerLayout = findViewById(R.id.test_drawer);
        navigationView = findViewById(R.id.nav_view2);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_recipe, R.string.open_recipe);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        initializeVariablesForActivity();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.something:
                    Intent goIngredients = new Intent(Activity.this, Ingredients.class);
                    startActivity(goIngredients);
                    break;
            }
            return false;
        });

    }
}