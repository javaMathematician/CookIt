package org.slovenlypolygon.recipes.backend.newdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
        Dish.class,
        Component.class,
        DishComponentCrossRef.class
}, version = 1, exportSchema = false)
public abstract class GlobalDatabase extends RoomDatabase {
    public abstract DAO getDAO();
}
