package org.slovenlypolygon.recipes.frontend;

import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Dish;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

public class FrontendDish extends Dish implements Comparable<FrontendDish> {
    private Set<String> selectedIngredients = new HashSet<>();
    private Set<String> restIngredients = new HashSet<>();

    public FrontendDish(int dishID, String dishName, String dishImageURL, String dishURL) {
        super(dishID, dishName, dishImageURL, dishURL);
    }

    public FrontendDish(FrontendDish other) {
        super(other);

        this.restIngredients = other.restIngredients;
        this.selectedIngredients = other.selectedIngredients;
    }

    public Set<String> getSelectedIngredients() {
        return selectedIngredients;
    }

    public void setSelectedIngredients(Set<String> selectedIngredients) {
        this.selectedIngredients = selectedIngredients;
    }

    public Set<String> getRestIngredients() {
        return restIngredients;
    }

    public void setRestIngredients(Set<String> restIngredients) {
        this.restIngredients = restIngredients;
    }

    @Override
    public int compareTo(FrontendDish o) {
        return Comparator.comparingInt((ToIntFunction<FrontendDish>) value -> value.selectedIngredients.size()).compare(this, o);
    }
}
