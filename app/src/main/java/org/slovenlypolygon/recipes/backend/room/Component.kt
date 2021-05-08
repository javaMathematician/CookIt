package org.slovenlypolygon.recipes.backend.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.room.rawobjects.DishComponentCrossReference
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawComponent
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawDish

data class Component(
    @Embedded val rawComponent: RawComponent,
    @Relation(
        parentColumn = "componentID",
        entityColumn = "dishID",
        associateBy = Junction(DishComponentCrossReference::class)
    )
    val dishes: List<RawDish>
)