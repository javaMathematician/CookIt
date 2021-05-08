package org.slovenlypolygon.recipes.backend.room.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dish(
    @PrimaryKey val dishID: Int,
    val dishImageURL: String?,
    val dishName: String,
    val dishURL: String?
)