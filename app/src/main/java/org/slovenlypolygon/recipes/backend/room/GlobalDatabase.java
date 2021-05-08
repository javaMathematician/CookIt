package org.slovenlypolygon.recipes.backend.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.slovenlypolygon.recipes.backend.room.rawobjects.DishComponentCrossReference;
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawComponent;
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawDirtyComponent;
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawDish;
import org.slovenlypolygon.recipes.backend.room.rawobjects.RawStep;

@Database(entities = {
        DishComponentCrossReference.class,
        RawDirtyComponent.class,
        RawComponent.class,
        RawDish.class,
        RawStep.class
}, version = 1, exportSchema = false)
public abstract class GlobalDatabase extends RoomDatabase {
    public abstract DAO getDAO();
}
