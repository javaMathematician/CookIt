package org.slovenlypolygon.recipes.backend.newdatabase.dataclasses

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.newdatabase.junctions.DishComponentCross
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Component
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Dish

data class ComponentWithDishes(
        @Embedded var ingredient: Component,
        @Relation(
                parentColumn = "componentID",
                entity = Dish::class,
                entityColumn = "dishID",
                associateBy = Junction(
                        value = DishComponentCross::class,
                        parentColumn = "componentID",
                        entityColumn = "dishID",
                )
        ) var componentWithDishes: List<Dish>
)
