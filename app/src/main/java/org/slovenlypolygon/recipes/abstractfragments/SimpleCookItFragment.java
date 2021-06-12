package org.slovenlypolygon.recipes.abstractfragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

public abstract class SimpleCookItFragment extends Fragment {
    protected MainActivity activity;

    @Override
    public void onActivityCreated(@Nullable @javax.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) requireActivity();
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.getSearchView().setVisibility(View.GONE);
        activity.getSupportActionBar().setTitle(R.string.app_name);
    }
}
