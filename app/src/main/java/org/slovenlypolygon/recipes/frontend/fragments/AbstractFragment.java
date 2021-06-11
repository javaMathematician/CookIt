package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

import java.util.Objects;

public abstract class AbstractFragment extends Fragment {
    protected SearchView searchView;
    protected MainActivity activity;
    protected boolean downloadQ;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) requireActivity();

        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(R.string.app_name);
        searchView = activity.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTextChanged(newText.toLowerCase());
                return false;
            }
        });
    }

    protected abstract void searchTextChanged(String newText);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);

        downloadQ = ((SettingsFragment) Objects.requireNonNull(getParentFragmentManager().findFragmentByTag(getString(R.string.backend_settings_fragment_tag)))).downloadPicturesQ();
    }

    @Override
    public void onStart() {
        super.onStart();
        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
    }
}