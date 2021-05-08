package org.slovenlypolygon.recipes.backend.room.rawobjects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "rawIngredient",
    foreignKeys = [
        ForeignKey(entity = RawDish::class, parentColumns = ["dishID"], childColumns = ["dishID"])
    ]
)
data class RawDirtyComponent(
    @PrimaryKey val rawIngredientID: Int,
    val dishID: Int,
    val content: String
)
