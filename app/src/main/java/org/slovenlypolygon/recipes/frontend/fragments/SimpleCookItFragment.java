package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.SettingsFragment;

import java.util.Objects;

public abstract class SimpleCookItFragment extends Fragment {
    protected MainActivity activity;
    protected boolean downloadQ;

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
        downloadQ = ((SettingsFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_settings_fragment_tag)))).downloadPicturesQ();
    }
}
