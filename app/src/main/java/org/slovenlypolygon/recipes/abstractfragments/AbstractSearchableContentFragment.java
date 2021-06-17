package org.slovenlypolygon.recipes.abstractfragments;

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
    protected String savedSearchQuery = "";
    private SearchView searchView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(R.string.app_name);
        searchView = activity.getSearchView();

        searchView.setVisibility(View.VISIBLE);
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
        searchView.setIconified(savedSearchQuery.isEmpty());
        searchView.setQuery(savedSearchQuery, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        savedSearchQuery = searchView.getQuery().toString();
    }
}