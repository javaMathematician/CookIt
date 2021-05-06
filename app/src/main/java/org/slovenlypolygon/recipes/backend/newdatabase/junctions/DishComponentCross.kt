package org.slovenlypolygon.recipes.backend.newdatabase.junctions

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Component
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Dish

@Entity(tableName = "DishWithIngredients",
        foreignKeys = [
            ForeignKey(entity = Component::class, parentColumns = ["componentID"], childColumns = ["componentID"]),
            ForeignKey(entity = Dish::class, parentColumns = ["dishID"], childColumns = ["dishID"]),
        ])
data class DishComponentCross(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val dishID: Int,
        val componentID: Int,
)