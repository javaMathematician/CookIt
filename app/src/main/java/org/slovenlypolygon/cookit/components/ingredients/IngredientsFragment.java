package org.slovenlypolygon.cookit.components.ingredients;

import org.slovenlypolygon.cookit.components.AbstractComponentsFragment;
import org.slovenlypolygon.cookit.components.entitys.ComponentType;

public class IngredientsFragment extends AbstractComponentsFragment {
    @Override
    protected ComponentType setDataSource() {
        return ComponentType.INGREDIENT;
    }
}
