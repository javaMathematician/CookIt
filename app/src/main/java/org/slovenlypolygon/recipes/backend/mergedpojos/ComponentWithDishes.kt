package org.slovenlypolygon.recipes.backend.mergedpojos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.crossreferences.DishComponentCrossReference
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent
import org.slovenlypolygon.recipes.backend.rawobjects.RawDish

data class ComponentWithDishes(
    @Embedded
    var component: RawComponent,

    @Relation(
        parentColumn = "componentID",
        entityColumn = "dishID",
        associateBy = Junction(DishComponentCrossReference::class)
    )
    var dishes: List<RawDish>
)