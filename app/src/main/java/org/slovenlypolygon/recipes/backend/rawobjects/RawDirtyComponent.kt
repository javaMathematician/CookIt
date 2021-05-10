package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rawIngredient",
    foreignKeys = [
        ForeignKey(entity = RawDish::class, parentColumns = ["dishID"], childColumns = ["dishID"])
    ],
    indices = [
        Index(value = ["rawIngredientID"], name = "rawIngredientIDIndex", unique = true),
        Index(value = ["dishID"], name = "rawIngredientDishIDIndex")
    ]
)
data class RawDirtyComponent(
    @PrimaryKey val rawIngredientID: Int,
    val dishID: Int,
    val content: String
)
