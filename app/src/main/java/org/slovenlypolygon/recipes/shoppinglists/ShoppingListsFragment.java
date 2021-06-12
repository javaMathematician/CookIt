package org.slovenlypolygon.recipes.shoppinglists;

import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.abstractfragments.SimpleCookItFragment;
import org.slovenlypolygon.recipes.backend.database.DatabaseFragment;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;
import org.slovenlypolygon.recipes.backend.picasso.PicassoBuilder;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShoppingListsFragment extends SimpleCookItFragment {
    protected DishComponentDAO dao;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shopping_lists_fragment, container, false);
        setRetainInstance(true);

        recyclerView = rootView.findViewById(R.id.shoppingListsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1));

        ShoppingListAdapter adapter = new ShoppingListAdapter();
        recyclerView.setAdapter(adapter);

        dao = ((DatabaseFragment) getParentFragmentManager().findFragmentByTag(getString(R.string.backend_database_fragment_tag))).getDishComponentDAO();
        dao.getShoppingLists()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lists -> {
                    adapter.addList(lists);
                    adapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchView searchView = requireActivity().findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
    }
}
