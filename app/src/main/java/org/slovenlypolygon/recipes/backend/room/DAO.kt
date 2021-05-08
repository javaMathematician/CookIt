package org.slovenlypolygon.recipes.backend.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DAO {
    @Transaction
    @Query("SELECT * FROM dish")
    fun getDishWithComponents(): List<DishWithComponents>

    @Transaction
    @Query("SELECT * FROM dish WHERE dish.dishID = :id")
    fun getDishWithComponentsByID(id: Int): DishWithComponents
}