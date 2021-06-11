package org.slovenlypolygon.recipes.components.categories;

import org.slovenlypolygon.recipes.components.AbstractComponentsFragment;
import org.slovenlypolygon.recipes.components.entitys.ComponentType;

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
