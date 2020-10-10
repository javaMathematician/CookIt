package org.slovenlypolygon.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchView;
    private TextView topTextViewOnToolbar;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        searchView.setOnClickListener(v -> searchView.setIconified(false));
    }

    public void cardClicked(View view) {
        counter++;
        toolbar.setBackgroundColor(counter % 2 == 0? Color.argb(255, 180, 180, 180) : Color.argb(255, 255, 10, 10));
    }
}