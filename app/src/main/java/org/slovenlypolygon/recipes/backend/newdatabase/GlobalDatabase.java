package org.slovenlypolygon.recipes.backend.newdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.slovenlypolygon.recipes.backend.newdatabase.junctions.DishComponentCross;
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Component;
import org.slovenlypolygon.recipes.backend.newdatabase.mainonjects.Dish;

@Database(entities = {
        Dish.class,
        Component.class,
        DishComponentCross.class
}, version = 1, exportSchema = false)
public abstract class GlobalDatabase extends RoomDatabase {
    public abstract DAO getDAO();
}
