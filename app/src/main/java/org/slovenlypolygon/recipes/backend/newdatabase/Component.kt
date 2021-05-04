package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Component(
        @PrimaryKey val componentID: Int,
        val componentName: String,
        val qIsIngredient: Int,
        val componentImageURL: String?
)
