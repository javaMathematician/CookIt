package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "step",
    foreignKeys = [
        ForeignKey(entity = RawDish::class, parentColumns = ["dishID"], childColumns = ["dishID"])
    ]
)
data class RawStep(
    @PrimaryKey val stepID: Int,
    val dishID: Int,
    val localOrder: Int,
    val stepImageURL: String?,
    val stepText: String
)
