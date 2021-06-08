package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality.componentpolymorphism;

import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;

public class IngredientsFragment extends AbstractComponentsFragment {
    public IngredientsFragment() {
        super();
    }

    @Override
    protected ComponentType setDataSource() {
        return ComponentType.INGREDIENT;
    }
}
