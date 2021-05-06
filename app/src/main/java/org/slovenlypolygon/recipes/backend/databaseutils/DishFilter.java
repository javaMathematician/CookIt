package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.common.collect.Sets;

import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.DishComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DishFilter {
    private final List<? extends Dish> assortment;

    private Set<DishComponent> dishComponents;
    private Set<String> dishComponentNames;

    public DishFilter(List<? extends Dish> assortment) {
        this.assortment = assortment;
    }

    public void setComponents(Set<DishComponent> dishComponents) {
        this.dishComponents = dishComponents;
        this.dishComponentNames = dishComponents.parallelStream().map(DishComponent::getName).collect(Collectors.toSet());
    }

    public List<Dish> getMatchingList() {
        List<Dish> dishList = new ArrayList<>();

        for (Dish dish : assortment) {
            boolean passedIngredients = dishComponents == null || containsAnyComponent(dish.getRecipeIngredients());
            boolean passedCategories = dishComponents == null || containsAnyComponent(dish.getCategories());

            if (passedIngredients || passedCategories) {
                dishList.add(dish);
            }
        }

        return dishList;
    }

    private boolean containsAnyComponent(List<String> provided) {
        Set<String> set = new HashSet<>(provided);
        return !Sets.intersection(set, dishComponentNames).isEmpty();
    }
}
