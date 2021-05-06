package org.slovenlypolygon.recipes.backend.newdatabase

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DishWithIngredients(
        @Embedded var dish: Dish,
        @Relation(
                parentColumn = "dishID",
                entity = Component::class,
                entityColumn = "componentID",
                associateBy = Junction(
                        value = DishIngredientCross::class,
                        parentColumn = "dishID",
                        entityColumn = "dishID"
                )
        ) var dishWithComponents: List<Component>
)
