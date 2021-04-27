package org.slovenlypolygon.recipes.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

import java.util.Objects;

public abstract class AbstractFragment extends Fragment {
    protected DrawerLayout drawerLayout;
    protected SearchView searchView;
    protected Toolbar toolbar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.app_name);


        drawerLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.drawerLayout);
        searchView = getActivity().findViewById(R.id.searchView);
        toolbar = getActivity().findViewById(R.id.toolbar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTextChanged(newText);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
    }
}