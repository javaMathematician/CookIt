package org.slovenlypolygon.recipes.backend;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.slovenlypolygon.recipes.backend.rawobjects.DishComponentCrossReference;
import org.slovenlypolygon.recipes.backend.rawobjects.RawComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDirtyComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDish;
import org.slovenlypolygon.recipes.backend.rawobjects.RawStep;

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
