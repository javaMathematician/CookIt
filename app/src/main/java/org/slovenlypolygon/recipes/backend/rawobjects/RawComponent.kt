package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "component", indices = [
        Index(value = ["componentID"], name = "componentIDIndex", unique = true)
    ]
)
data class RawComponent(
    @PrimaryKey val componentID: Int,
    val componentImageURL: String?,
    val componentName: String,
    val qIsIngredient: Int
)