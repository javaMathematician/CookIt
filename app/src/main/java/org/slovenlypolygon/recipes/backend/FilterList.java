package org.slovenlypolygon.recipes.backend;

import android.widget.TextView;
import androidx.cardview.widget.CardView;
import org.slovenlypolygon.recipes.R;

import java.util.List;
import java.util.stream.Collectors;

public class FilterList implements Runnable {
    private final List<CardView> cards;
    private List<CardView> output;
    private String newText;

    public FilterList(List<CardView> cards, String newText) {
        this.newText = newText;
        this.cards = cards;
    }

    @Override
    public void run() {
        output = cards.stream().filter(t -> ((TextView) t.findViewById(R.id.textOnCard))
                .getText()
                .toString()
                .toLowerCase()
                .trim()
                .contains(newText.toLowerCase().trim())).collect(Collectors.toList());
    }

    public List<CardView> getOutput() {
        return output;
    }
}
