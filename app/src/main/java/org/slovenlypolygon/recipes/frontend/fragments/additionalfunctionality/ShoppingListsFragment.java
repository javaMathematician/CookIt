package org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.additionalfunctionality.ShoppingList;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShoppingListsFragment extends Fragment {
    protected DishComponentDAO dao;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shopping_lists_fragment, container, false);
        setRetainInstance(true);

        linearLayout = rootView.findViewById(R.id.shoppingListsLinearLayout);

        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_frament_tag))).getDishComponentDAO();
        dao.getShoppingLists()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addCards, Throwable::printStackTrace);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchView searchView = getActivity().findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
    }

    private void addCards(List<ShoppingList> shoppingLists) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for (ShoppingList shoppingList : shoppingLists) {
            CardView cardView = (CardView) layoutInflater.inflate(R.layout.list_card, linearLayout, false);
        }
    }
}
