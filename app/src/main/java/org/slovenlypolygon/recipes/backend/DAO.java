package org.slovenlypolygon.recipes.backend;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.slovenlypolygon.recipes.backend.mergedpojos.ComponentWithDishes;
import org.slovenlypolygon.recipes.backend.mergedpojos.DishWithComponents;
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDirtyComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDish;
import org.slovenlypolygon.recipes.backend.rawobjects.RawStep;

import java.util.List;

@Dao
public abstract class DAO {
    @Transaction
    @Query("SELECT * FROM dish")
    public abstract List<RawDish> getAllDishes();

    @Transaction
    @Query("SELECT * FROM dish WHERE dishID = :id")
    public abstract RawDish getDishByID(int id);

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 0")
    public abstract List<RawComponent> getAllCategories();

    @Transaction
    @Query("SELECT * FROM component WHERE componentID = :id")
    public abstract RawComponent getComponentByID(int id);

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 1")
    public abstract List<RawComponent> getAllIngredients();

    @Transaction
    @Query("SELECT * FROM component WHERE componentID = :id")
    public abstract List<ComponentWithDishes> getComponentWithDishesFromComponentIDs(int id);

    @Transaction
    @Query("SELECT * FROM dish WHERE dishID = :id")
    public abstract List<DishWithComponents> getDishWithComponentsFromDishID(int id);

    @Transaction
    @Query("SELECT * FROM component WHERE componentID IN (:ids)")
    public abstract List<ComponentWithDishes> getComponentWithDishesFromComponentIDs(List<Integer> ids);

    @Transaction
    @Query("SELECT * FROM step WHERE dishID = :id")
    public abstract List<RawStep> getStepsFromDishID(int id);

    @Transaction
    @Query("SELECT * FROM rawIngredient WHERE dishID = :id")
    public abstract List<RawDirtyComponent> getDirtyComponentsFromDishID(int id);
}