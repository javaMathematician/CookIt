package org.slovenlypolygon.recipes.backend.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawComponent

@Dao
interface DAO {
    @Transaction
    @Query("SELECT * FROM dish")
    fun getAllDishes(): Set<Dish>

    @Transaction
    @Query("SELECT * FROM dish WHERE dishID = :id")
    fun getDishByID(id: Int): Dish

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 0")
    fun getAllCategories(): Set<RawComponent>

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 1")
    fun getAllIngredients(): Set<RawComponent>
}