package org.slovenlypolygon.recipes.backend.mergedpojos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.crossreferences.DishComponentCrossReference
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent
import org.slovenlypolygon.recipes.backend.rawobjects.RawDirtyComponent
import org.slovenlypolygon.recipes.backend.rawobjects.RawDish
import org.slovenlypolygon.recipes.backend.rawobjects.RawStep

data class DishWithComponents(
    @Embedded var dish: RawDish,

    @Relation(
        parentColumn = "dishID",
        entityColumn = "componentID",
        associateBy = Junction(DishComponentCrossReference::class)
    ) var components: List<RawComponent>,

    @Relation(
        parentColumn = "dishID",
        entityColumn = "stepID",
        associateBy = Junction(RawStep::class)
    ) var steps: List<RawStep>,

    @Relation(
        parentColumn = "dishID",
        entityColumn = "rawIngredientID",
        associateBy = Junction(RawDirtyComponent::class)
    ) val rawDirtyComponents: List<RawDirtyComponent>
)