package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.ComponentType;

public class IngredientsFragment extends AbstractComponentsFragment {
    @Override
    protected ComponentType setDataSource() {
        return ComponentType.INGREDIENT;
    }
}
