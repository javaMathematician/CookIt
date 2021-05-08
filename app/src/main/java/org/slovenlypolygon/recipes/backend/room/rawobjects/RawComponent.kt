package org.slovenlypolygon.recipes.backend.room.rawobjects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "component")
data class RawComponent(
    @PrimaryKey val componentID: Int,
    val componentImageURL: String?,
    val componentName: String,
    val qIsIngredient: Int
)