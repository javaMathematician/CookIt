package org.slovenlypolygon.recipes.backend.roomattempts;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.slovenlypolygon.recipes.backend.roomattempts.dao.ComponentPartDAO;
import org.slovenlypolygon.recipes.backend.roomattempts.dao.RoomDishPartDAO;
import org.slovenlypolygon.recipes.backend.roomattempts.parts.RoomComponentPart;
import org.slovenlypolygon.recipes.backend.roomattempts.parts.RoomDishPart;

@Database(entities = {RoomComponentPart.class, RoomDishPart.class}, version = 3, exportSchema = false)
public abstract class RepoDatabase extends RoomDatabase {
    public abstract ComponentPartDAO getComponentDAO();

    public abstract RoomDishPartDAO getRoomDishPartDao();
}
