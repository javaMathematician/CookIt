package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dish",
    indices = [
        Index(value = ["dishID"], name = "dishIDIndex", unique = true)
    ]
)
data class RawDish(
    @PrimaryKey var dishID: Int,
    var dishImageURL: String?,
    var dishName: String,
    var dishURL: String?,
)