package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.slovenlypolygon.recipes.backend.newdatabase.dataclasses.ComponentWithDishes
import org.slovenlypolygon.recipes.backend.newdatabase.dataclasses.DishWithComponents

@Dao
interface DAO {
    @Transaction
    @Query("SELECT DISTINCT * FROM dish WHERE dish.dishID = :id")
    fun getDishWithIngredients(id: Int): List<DishWithComponents>

    @Transaction
    @Query("SELECT * FROM component WHERE component.componentID = :id")
    fun getComponentWithDishes(id: Int): List<ComponentWithDishes>
}