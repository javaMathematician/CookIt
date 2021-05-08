package org.slovenlypolygon.recipes.backend.room.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Component(
    @PrimaryKey val componentID: Int,
    val componentImageURL: String?,
    val componentName: String,
    val qIsIngredient: Int
)
