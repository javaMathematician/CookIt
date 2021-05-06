package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DAO {
    @Transaction
    @Query("SELECT * FROM dish WHERE dish.dishID = :id")
    fun getDishWithIngredients(id: Int): List<DishWithIngredients>
}