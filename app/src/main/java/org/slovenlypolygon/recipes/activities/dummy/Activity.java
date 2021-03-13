package org.slovenlypolygon.recipes.activities.dummy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.activities.Ingredients;

public class Activity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private void initializeVariablesForActivity() {
        drawerLayout = findViewById(R.id.test_drawer);
        navigationView = findViewById(R.id.nav_view2);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_recipe, R.string.open_recipe);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        initializeVariablesForActivity();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.toIngredients) {
                startActivity(new Intent(this, Ingredients.class));
            }
            return false;
        });

    }
}