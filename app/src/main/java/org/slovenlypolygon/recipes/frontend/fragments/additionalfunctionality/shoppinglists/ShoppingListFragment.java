package org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality.shoppinglists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.frontend.fragments.AbstractFragment;

public class ShoppingListFragment extends Fragment {
    protected DishComponentDAO dao;

    private RecyclerView recyclerView;

    private void initializeVariables(View root) {
        recyclerView = root.findViewById(R.id.shoppingListsRecyclerView);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shopping_lists_fragment, container, false);
        setRetainInstance(true);

        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag("databaseFragment")).getDishComponentDAO();

        initializeVariables(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchView searchView = getActivity().findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
    }

}
