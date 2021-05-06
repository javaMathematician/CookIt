package org.slovenlypolygon.recipes.backend.newdatabase.mainonjects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dish(
        @PrimaryKey val dishID: Int,
        val dishName: String,
        val dishImageURL: String?,
        val dishURL: String?
)
