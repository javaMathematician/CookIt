package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DAO {
    @Transaction
    @Query("SELECT * FROM dish")
    fun getDishWithIngredients(): List<DishWithIngredients>
}