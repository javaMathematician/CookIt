package org.slovenlypolygon.recipes.backend.newdatabase.dataclasses

import androidx.room.Embedded
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Component
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Dish

data class Dummy(
        @Embedded val dish: Dish,
        @Embedded val components: List<Component>
)
