package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "step",
    foreignKeys = [
        ForeignKey(entity = RawDish::class, parentColumns = ["dishID"], childColumns = ["dishID"])
    ],
    indices = [
        Index(value = ["stepID"], name = "stepIDIndex", unique = true),
        Index(value = ["dishID"], name = "stepDishIDIndex")
    ]
)
data class RawStep(
    @PrimaryKey val stepID: Int,
    val dishID: Int,
    val localOrder: Int,
    val stepImageURL: String?,
    val stepText: String
)
