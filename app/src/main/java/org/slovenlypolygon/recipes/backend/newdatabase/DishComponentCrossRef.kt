package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "component_dish",
        primaryKeys = ["dish_id", "component_id"],
        foreignKeys = [
            ForeignKey(entity = Dish::class, childColumns = ["dish_id"], parentColumns = ["id"]),
            ForeignKey(entity = Component::class, childColumns = ["component_id"], parentColumns = ["id"])
        ])
data class DishComponentCrossRef(
        @ColumnInfo(name = "dish_id") val dishID: Int,
        @ColumnInfo(name = "component_id") val componentID: Int,
)