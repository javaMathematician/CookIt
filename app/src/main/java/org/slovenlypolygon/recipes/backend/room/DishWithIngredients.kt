package org.slovenlypolygon.recipes.backend.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.slovenlypolygon.recipes.backend.room.objects.Component

@Entity(
    foreignKeys = [
        ForeignKey(entity = Component::class, parentColumns = ["componentID"], childColumns = ["componentID"]),
        ForeignKey(entity = Dish::class, parentColumns = ["dishID"], childColumns = ["dishID"]),
    ]
)
data class DishWithIngredients(
    @PrimaryKey val id: Int,
    val dishID: Int,
    val componentID: Int
)