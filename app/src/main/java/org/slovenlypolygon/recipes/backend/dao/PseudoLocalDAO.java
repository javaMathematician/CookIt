package org.slovenlypolygon.recipes.backend.dao;

import org.slovenlypolygon.recipes.backend.ConstructedDish;

import java.util.Set;

public class PseudoLocalDAO {
    private Set<ConstructedDish> constructedDishSet;

    public PseudoLocalDAO(Set<ConstructedDish> constructedDishSet) {
        this.constructedDishSet = constructedDishSet;
    }
}
