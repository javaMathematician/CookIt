package org.slovenlypolygon.recipes.components.ingredients;

import org.slovenlypolygon.recipes.components.AbstractComponentsFragment;
import org.slovenlypolygon.recipes.components.entitys.ComponentType;

public class IngredientsFragment extends AbstractComponentsFragment {
    @Override
    protected ComponentType setDataSource() {
        return ComponentType.INGREDIENT;
    }
}
