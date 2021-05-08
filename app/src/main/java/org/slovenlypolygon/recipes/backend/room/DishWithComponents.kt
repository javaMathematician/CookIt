package org.slovenlypolygon.recipes.backend.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.slovenlypolygon.recipes.backend.room.objects.Component

data class DishWithComponents(
    @Embedded val dish: Dish,
    @Relation(
        parentColumn = "dishID",
        entityColumn = "componentID",
        associateBy = Junction(DishWithIngredients::class)
    )
    val components: List<Component>
)