package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish")
data class RawDish(
    @PrimaryKey val dishID: Int,
    val dishImageURL: String?,
    val dishName: String,
    val dishURL: String?
)