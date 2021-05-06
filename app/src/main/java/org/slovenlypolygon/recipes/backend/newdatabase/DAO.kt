package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Dao
import androidx.room.Query
import org.slovenlypolygon.recipes.backend.newdatabase.dataclasses.Dummy

@Dao
interface DAO {
    @Query("select dish.dishName, dish.dishImageURL, component.componentName, component.qIsIngredient, component.componentImageURL \n" +
            "from dish\n" +
            "join dishWithIngredients on (Dish.dishID = DishWithIngredients.dishID) \n" +
            "join component on (component.componentID = DishWithIngredients.componentID)")
    fun getRaw(): List<Dummy>
}