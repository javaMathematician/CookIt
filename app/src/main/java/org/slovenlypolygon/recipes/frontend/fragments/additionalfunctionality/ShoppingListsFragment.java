package org.slovenlypolygon.recipes.frontend.fragments.additionalfunctionality;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.backend.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.mainobjects.additionalfunctionality.ShoppingList;
import org.slovenlypolygon.recipes.frontend.fragments.SimpleCookItFragment;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShoppingListsFragment extends SimpleCookItFragment {
    protected DishComponentDAO dao;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shopping_lists_fragment, container, false);
        setRetainInstance(true);

        linearLayout = rootView.findViewById(R.id.shoppingListsLinearLayout);

        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag))).getDishComponentDAO();
        dao.getShoppingLists()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addCards, Throwable::printStackTrace);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchView searchView = requireActivity().findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
    }

    private void addCards(List<ShoppingList> shoppingLists) {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());

        for (ShoppingList shoppingList : shoppingLists) {
            CardView cardView = (CardView) layoutInflater.inflate(R.layout.list_card, linearLayout, false);

            TextView name = cardView.findViewById(R.id.listName);
            name.setText(shoppingList.getDish().getName());

            TextView content = cardView.findViewById(R.id.listContent);
            content.setText(Joiner.on(",\n").join(shoppingList.getDish().getDirtyIngredients()));

            linearLayout.addView(cardView);
        }
    }
}
