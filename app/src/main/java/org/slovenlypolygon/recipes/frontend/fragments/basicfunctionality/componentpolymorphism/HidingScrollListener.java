package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;

    private boolean controlsVisible = true;
    private int scrolledDistance;

    @Override
    public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < 0 && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if (controlsVisible ? dy > 0 : dy < 0) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();
}
