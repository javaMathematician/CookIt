package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "component")
data class Component(
        @PrimaryKey @ColumnInfo(name = "id") val componentID: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "is_ingredient") val isIngredient: Int,
        @ColumnInfo(name = "image_url") val imageURL: String?
)
