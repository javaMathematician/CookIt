package org.slovenlypolygon.recipes.backend.newdatabase.dataclasses

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.newdatabase.junctions.DishComponentCross
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Component
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Dish

data class DishWithComponents(
        @Embedded var dish: Dish,
        @Relation(
                parentColumn = "dishID",
                entity = Component::class,
                entityColumn = "componentID",
                associateBy = Junction(value = DishComponentCross::class)
        ) var dishWithComponents: List<Component>
)
