package org.slovenlypolygon.recipes.frontend.adapters;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;

import java.util.List;

public class StepByStepAdapter extends RecyclerView.Adapter<StepByStepAdapter.StepViewHolder> {
    private final List<List<String>> instructions;

    public StepByStepAdapter(List<List<String>> instructions) {
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepByStepAdapter.StepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.step_by_step_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String text = instructions.get(position).get(0);
        String url = instructions.get(position).get(1);

        if (url.length() != 0) {
            Picasso.get()
                    .load(url)
                    .error(R.drawable.sample_dish_for_error)
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.imageView);

            holder.expandButton.setVisibility(View.VISIBLE);
            holder.cardView.setOnClickListener(v -> {
                TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());

                if (holder.constraintLayout.getVisibility() == View.GONE) {
                    holder.constraintLayout.setVisibility(View.VISIBLE);
                    holder.expandButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    holder.constraintLayout.setVisibility(View.GONE);
                    holder.expandButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            });
        }

        holder.stepText.setText(text);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        private final Button expandButton;
        private final CardView cardView;
        private final TextView stepText;
        private final ImageView imageView;
        private final ConstraintLayout constraintLayout;

        public StepViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.stepByStepCard);
            constraintLayout = itemView.findViewById(R.id.expandableStep);

            expandButton = itemView.findViewById(R.id.expandStepButton);
            imageView = itemView.findViewById(R.id.stepByStepImage);
            stepText = itemView.findViewById(R.id.stepText);
        }
    }
}
