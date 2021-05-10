package org.slovenlypolygon.recipes.backend.dao;

import com.google.common.collect.Sets;

import org.slovenlypolygon.recipes.backend.ConstructedDish;
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent;

import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PseudoLocalDAO {
    private final Set<ConstructedDish> constructedDishSet;

    public PseudoLocalDAO(Set<ConstructedDish> constructedDishSet) {
        this.constructedDishSet = constructedDishSet;
    }

    public Observable<ConstructedDish> getDishesFromComponentIDs(Set<Integer> componentIDs) {
        return Observable.fromIterable(constructedDishSet)
                .subscribeOn(Schedulers.newThread())
                .filter(dish -> {
                    Set<Integer> dishIDs = dish.getDishWithComponents().getComponents().stream().map(RawComponent::getComponentID).collect(Collectors.toSet());
                    return !Sets.intersection(dishIDs, componentIDs).isEmpty();
                });
    }
}
