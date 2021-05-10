package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish")
data class RawDish(
    @PrimaryKey var dishID: Int,
    var dishImageURL: String?,
    var dishName: String,
    var dishURL: String?,
)