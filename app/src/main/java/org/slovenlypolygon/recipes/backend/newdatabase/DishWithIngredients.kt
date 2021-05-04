package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DishWithIngredients(
        @Embedded val dish: Dish,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        DishComponentCrossRef::class,
                        parentColumn = "dish_id",
                        entityColumn = "component_id"
                )
        ) val components: List<Component>
)