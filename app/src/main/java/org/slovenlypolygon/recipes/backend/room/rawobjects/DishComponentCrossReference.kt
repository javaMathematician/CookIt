package org.slovenlypolygon.recipes.backend.room.rawobjects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "dishComponentCrossReference",
    foreignKeys = [
        ForeignKey(entity = RawComponent::class, parentColumns = ["componentID"], childColumns = ["componentID"]),
        ForeignKey(entity = RawDish::class, parentColumns = ["dishID"], childColumns = ["dishID"]),
    ]
)
data class DishComponentCrossReference(
    @PrimaryKey val id: Int,
    val dishID: Int,
    val componentID: Int
)