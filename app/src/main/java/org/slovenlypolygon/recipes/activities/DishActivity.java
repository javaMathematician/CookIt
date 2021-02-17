package org.slovenlypolygon.recipes.activities;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.slovenlypolygon.recipes.R;

import java.util.Objects;

public class DishActivity extends AppCompatActivity {
    private LinearLayout cardHolder;
    private Button arrowBtn;
    private ConstraintLayout expandableStep;
    private CardView cardView;


    private void initializeVariablesForStepByStep() {
        cardHolder = findViewById(R.id.stepByStepLinearLayout);
        expandableStep = findViewById(R.id.expandableStep);
        arrowBtn = findViewById(R.id.expandStepButton);
        cardView = findViewById(R.id.stepByStepCard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_by_step);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeVariablesForStepByStep();

        //Добавлять cardView на cardHolder(У меня постоянно выдает ошибку Cannot add a null child view to a ViewGroup), но мб я дурачок)

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableStep.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableStep.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableStep.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });
    }
}