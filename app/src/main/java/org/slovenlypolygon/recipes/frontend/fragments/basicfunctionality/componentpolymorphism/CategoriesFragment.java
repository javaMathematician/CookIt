package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;

public class CategoriesFragment extends AbstractComponentsFragment {
    @Override
    protected ComponentType setDataSource() {
        return ComponentType.CATEGORY;
    }

    @Override
    protected void addCallbacks() {
        super.addCallbacks();
        componentAdapter.clearItemLongClickCallback();
    }
}
