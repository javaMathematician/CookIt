package org.slovenlypolygon.recipes.backend

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent

@Dao
interface DAO {
    @Transaction
    @Query("SELECT * FROM dish")
    fun getAllDishes(): List<Dish>

    @Transaction
    @Query("SELECT * FROM dish WHERE dishID = :id")
    fun getDishByID(id: Int): Dish

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 0")
    fun getAllCategories(): List<RawComponent>

    @Transaction
    @Query("SELECT * FROM component WHERE qIsIngredient = 1")
    fun getAllIngredients(): List<RawComponent>

    @Transaction
    @Query("SELECT * FROM component WHERE componentID in (:ids)")
    fun getNestedDishesFromComponentIDs(ids: List<Int>): List<Component>
}