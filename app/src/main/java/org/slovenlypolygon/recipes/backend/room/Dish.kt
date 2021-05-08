package org.slovenlypolygon.recipes.backend.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.room.rawobjects.*

data class Dish(
    @Embedded val dish: RawDish,
    @Relation(
        parentColumn = "dishID",
        entityColumn = "componentID",
        associateBy = Junction(DishComponentCrossReference::class)
    )
    val components: List<RawComponent>,

    @Relation(
        parentColumn = "dishID",
        entityColumn = "rawIngredientID",
        associateBy = Junction(RawDirtyComponent::class)
    )
    val rawComponents: List<RawDirtyComponent>,

    @Relation(
        parentColumn = "dishID",
        entityColumn = "stepID",
        associateBy = Junction(RawStep::class)
    )
    val steps: List<RawStep>
)