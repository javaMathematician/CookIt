package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import org.slovenlypolygon.recipes.R;

import java.util.Objects;

public abstract class AbstractSearchableContentFragment extends SimpleCookItFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(R.string.app_name);

        activity.getSearchView().setVisibility(View.VISIBLE);
        activity.getSearchView().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        activity.getSearchView().setVisibility(View.VISIBLE);
        activity.getSearchView().setIconified(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.getSearchView().setVisibility(View.VISIBLE);
        activity.getSearchView().setIconified(true);
    }
}