package org.slovenlypolygon.recipes.backend.mainobjects.additionalfunctionality

import org.slovenlypolygon.recipes.backend.mainobjects.basicfunctionality.Dish

data class ShoppingList(
    val id: Int,
    val dish: Dish
)