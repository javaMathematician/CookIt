package org.slovenlypolygon.recipes.backend.rawobjects

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "dish")
data class RawDish(
    @PrimaryKey var dishID: Int,
    var dishImageURL: String?,
    var dishName: String,
    var dishURL: String?,
) {
    @Ignore
    var components: List<RawComponent> = ArrayList()

    @Ignore
    var dirtyComponents: List<RawDirtyComponent> = ArrayList()

    @Ignore
    var steps: List<RawStep> = ArrayList()
}