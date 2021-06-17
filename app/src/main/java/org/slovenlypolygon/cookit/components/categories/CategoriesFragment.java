package org.slovenlypolygon.cookit.components.categories;

import org.slovenlypolygon.cookit.components.AbstractComponentsFragment;
import org.slovenlypolygon.cookit.components.entitys.ComponentType;

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
