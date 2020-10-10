package org.slovenlypolygon.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView topTextViewOnToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
    }

    public void mainButtonClicked(View view) {
        toolbar.setBackgroundColor(Color.argb(255, 255, 30, 30));
    }

    public void cardClicked(View view) {
        toolbar.setBackgroundColor(Color.argb(255,180,180,180));
    }
}