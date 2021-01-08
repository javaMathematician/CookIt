package org.slovenlypolygon.recipes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import org.slovenlypolygon.recipes.backend.backendcards.Generator;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button changeView;
    private Typeface customFont;
    private SearchView searchView;
    private ScrollView scrollView;
    private LayoutInflater inflater;
    private TextView topTextViewOnToolbar;
    private ImageButton scrollToTopButton;
    private LinearLayout allDishesCardHolder;

    private void initializeVariables() {
        inflater = LayoutInflater.from(this);
        changeView = findViewById(R.id.changeView);
        searchView = findViewById(R.id.searchView);
        scrollView = findViewById(R.id.scrollView);
        scrollToTopButton = findViewById(R.id.scrollToTopButton);
        allDishesCardHolder = findViewById(R.id.allDishesCardHolder);
        topTextViewOnToolbar = findViewById(R.id.topTextViewOnToolbar);
        searchView.setOnClickListener(v -> searchView.setIconified(false));

        customFont = Typeface.createFromAsset(getAssets(), "fonts/17651.ttf");
        scrollToTopButton.setVisibility(View.INVISIBLE);
        topTextViewOnToolbar.setTypeface(customFont);
        changeView.setTypeface(customFont);
    }

    private void generateCards() {
        Generator generator = new Generator(inflater);

        try {
            generator.setIngredientURLMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.urls)));
            generator.setDirtyToCleanedMapper(Deserializer.deserializeMap(getResources().openRawResource(R.raw.cleaned)));
            generator.setCustomFont(customFont);
            generator.setRoot(allDishesCardHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CardView cardView : generator.generate()) {
            allDishesCardHolder.addView(cardView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeVariables();
        generateCards();

        scrollToTopButton.setOnClickListener(t -> scrollView.smoothScrollTo(0, 0));

        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (oldScrollY - scrollY < 0 && scrollY > 2000) {
                if (scrollToTopButton.getVisibility() == View.INVISIBLE) {
                    scrollToTopButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                }

                scrollToTopButton.setVisibility(View.VISIBLE);
            } else {
                if (scrollToTopButton.getVisibility() == View.VISIBLE) {
                    scrollToTopButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
                }

                scrollToTopButton.setVisibility(View.INVISIBLE);
            }
        });
    }
}