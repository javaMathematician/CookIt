package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtil;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StepByStep extends AppCompatActivity {
    private Map<String, List<String>> map;
    private LinearLayout linearLayout;
    private Dish dish;

    private void initialize() {
        try (InputStream stream = getResources().openRawResource(R.raw.raw_ingredients)) {
            map = new Gson().fromJson(IOUtil.toString(stream), new TypeToken<Map<String, List<String>>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dish = getIntent().getParcelableExtra("dish");
        linearLayout = findViewById(R.id.stepByStepLinearLayout);

        Picasso.get()
                .load(dish.getImageURL())
                .error(R.drawable.sample_dish_for_error)
                .into((ImageView) findViewById(R.id.dishStepByStepImage));

        String ingredients = getResources().getString(R.string.you_will_need)
                + "\n    " + Joiner.on(",\n    ").join(map.getOrDefault(dish.getName(), new ArrayList<>())) + ".";
        TextView stepByStepIngredients = findViewById(R.id.stepByStepIngredients);
        stepByStepIngredients.setText(ingredients);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_by_step);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        addSteps();
    }

    private void addSteps() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        for (List<String> pair : dish.getRecipeInstructions()) {
            CardView cardView = (CardView) inflater.inflate(R.layout.step_by_step_card, linearLayout, false);

            Button expandButton = cardView.findViewById(R.id.expandStepButton);
            TextView stepText = cardView.findViewById(R.id.stepText);
            ImageView imageView = cardView.findViewById(R.id.stepByStepImage);
            ConstraintLayout constraintLayout = cardView.findViewById(R.id.expandableStep);

            String url = pair.get(0);
            String text = pair.get(1);

            if (url.length() != 0) {
                Picasso.get()
                        .load(url)
                        .error(R.drawable.sample_dish_for_error)
                        .into(imageView);

                expandButton.setVisibility(View.VISIBLE);
                cardView.setOnClickListener(v -> {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());

                        constraintLayout.setVisibility(View.VISIBLE);
                        expandButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    } else {
                        constraintLayout.setVisibility(View.GONE);
                        expandButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }
                });
            } else {
                stepText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            stepText.setText(text);
            linearLayout.addView(cardView);
        }
    }
}