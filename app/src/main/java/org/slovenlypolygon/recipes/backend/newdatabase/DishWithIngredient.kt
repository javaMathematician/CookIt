package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Entity

@Entity(primaryKeys = ["dishID", "componentID"])
data class DishWithIngredient(
        val dishID: Int,
        val componentID: Int,
)