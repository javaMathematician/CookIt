package org.slovenlypolygon.cookit.shoppinglists;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.cookit.R;
import org.slovenlypolygon.cookit.abstractfragments.SimpleCookItFragment;
import org.slovenlypolygon.cookit.backend.DatabaseFragment;
import org.slovenlypolygon.cookit.backend.DishComponentDAO;
import org.slovenlypolygon.cookit.utils.DeleteSubstrate;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShoppingListsFragment extends SimpleCookItFragment {
    protected DishComponentDAO dao;
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private TextView textView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shopping_lists_fragment, container, false);
        setRetainInstance(true);

        recyclerView = rootView.findViewById(R.id.shoppingListsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1));

        adapter = new ShoppingListAdapter();
        recyclerView.setAdapter(adapter);

        textView = rootView.findViewById(R.id.shoppingListEndOfPageText);

        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag))).getDishComponentDAO();
        dao.getShoppingLists()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lists -> {
                    adapter.addLists(lists);
                    checkQuantity();
                }, Throwable::printStackTrace);

        return rootView;
    }

    private void checkQuantity() {
        textView.setText(adapter.getItemCount() == 0 ? R.string.nothing_in_shopping_lists : R.string.page_end);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().<SearchView>findViewById(R.id.searchView).setVisibility(View.INVISIBLE);

        DeleteSubstrate deleteSubstrate = new DeleteSubstrate(requireActivity(), position -> {
            dao.removeFromShoppingList(adapter.getList(position).getDish());
            adapter.removeList(position);
            checkQuantity();
        });

        new ItemTouchHelper(deleteSubstrate.getItemTouchHelperCallback()).attachToRecyclerView(recyclerView);
    }
}
