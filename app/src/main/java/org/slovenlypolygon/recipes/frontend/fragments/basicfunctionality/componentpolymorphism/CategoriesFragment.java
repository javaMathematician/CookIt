package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import org.slovenlypolygon.recipes.backend.bridges.FragmentAdapterBridge;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoriesFragment extends AbstractComponentsFragment implements FragmentAdapterBridge {
    @Override
    protected void addData() {
        super.addData();

        dao.getComponentByType(ComponentType.CATEGORY)
                .subscribeOn(Schedulers.newThread())
                .buffer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(components -> {
                    dishComponentsAdapter.addComponents(components);
                    dishComponentsAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }
}
