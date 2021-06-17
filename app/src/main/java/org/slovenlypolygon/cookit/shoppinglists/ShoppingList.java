package org.slovenlypolygon.cookit.shoppinglists;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;
import org.slovenlypolygon.cookit.dishes.entitys.Dish;

import java.util.Comparator;

import javax.annotation.Nonnull;

public class ShoppingList implements Comparable<ShoppingList> {
    private final int id;
    @NotNull private final Dish dish;

    public ShoppingList(int id, @NotNull Dish dish) {
        this.id = id;
        this.dish = dish;
    }

    public final int getId() {
        return this.id;
    }

    @NotNull
    public final Dish getDish() {
        return this.dish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingList that = (ShoppingList) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("dish", dish)
                .toString();
    }

    @Override
    public int compareTo(ShoppingList o) {
        return Comparator.comparing((Function<ShoppingList, String>) input -> input.dish.getName()).compare(this, o);
    }
}