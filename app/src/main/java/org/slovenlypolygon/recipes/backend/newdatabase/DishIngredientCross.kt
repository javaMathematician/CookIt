package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "DishWithIngredients",
        foreignKeys = [
            ForeignKey(entity = Component::class, parentColumns = ["componentID"], childColumns = ["componentID"]),
            ForeignKey(entity = Dish::class, parentColumns = ["dishID"], childColumns = ["dishID"]),
        ])
data class DishIngredientCross(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val dishID: Int,
        val componentID: Int,
)