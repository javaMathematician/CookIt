package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteIngredientsFragment extends IngredientsFragment {
    @Override
    protected void addData() {
        super.addData();

        dao.getFavoriteComponents()
                .subscribeOn(Schedulers.newThread())
                .buffer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(components -> {
                    dishComponentsAdapter.addComponents(components);
                    dishComponentsAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }
}
