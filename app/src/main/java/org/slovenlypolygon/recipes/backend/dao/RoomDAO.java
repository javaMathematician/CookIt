package org.slovenlypolygon.recipes.backend.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.slovenlypolygon.recipes.backend.mergedpojos.DishWithComponents;
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDirtyComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDish;
import org.slovenlypolygon.recipes.backend.rawobjects.RawStep;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDAO {
    @Transaction
    @Query("SELECT * FROM dish")
    Flowable<List<RawDish>> getAllDishes();

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 0")
    Flowable<List<RawComponent>> getAllCategories();

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 1")
    Flowable<List<RawComponent>> getAllIngredients();

    @Transaction
    @Query("SELECT * FROM dish")
    Flowable<List<DishWithComponents>> getAllDishWithComponents();

    @Transaction
    @Query("SELECT * FROM step WHERE dishID = :id")
    Flowable<List<RawStep>> getStepsFromDishID(int id);

    @Transaction
    @Query("SELECT * FROM rawIngredient WHERE dishID = :id")
    Flowable<List<RawDirtyComponent>> getDirtyComponentsFromDishID(int id);
}