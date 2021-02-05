package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.slovenlypolygon.recipes.R;

import java.util.Objects;

public class DishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_by_step);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public void onBackPressed() {

    }
}