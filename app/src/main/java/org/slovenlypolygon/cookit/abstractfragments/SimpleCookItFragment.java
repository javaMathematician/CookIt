package org.slovenlypolygon.cookit.abstractfragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.cookit.MainActivity;
import org.slovenlypolygon.cookit.R;

public abstract class SimpleCookItFragment extends Fragment {
    protected MainActivity activity;
    protected SearchView searchView;

    @Override
    public void onActivityCreated(@Nullable @javax.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) requireActivity();
        searchView = activity.getSearchView();
    }

    @Override
    public void onResume() {
        super.onResume();

        searchView.setVisibility(View.GONE);
        activity.getSupportActionBar().setTitle(R.string.app_name);
    }
}
