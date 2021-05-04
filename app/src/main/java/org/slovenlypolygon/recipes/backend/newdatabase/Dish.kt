package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish")
data class Dish(
        @PrimaryKey @ColumnInfo(name = "id") val dishID: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "image_url") val imageURL: String?,
        @ColumnInfo(name = "dish_url") val dishURL: String?
)
