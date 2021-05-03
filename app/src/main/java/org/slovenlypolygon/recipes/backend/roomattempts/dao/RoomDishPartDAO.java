package org.slovenlypolygon.recipes.backend.roomattempts.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.slovenlypolygon.recipes.backend.roomattempts.parts.RoomDishPart;

import java.util.List;

@Dao
public interface RoomDishPartDAO {
    @Insert
    long insertDish(RoomDishPart roomDishPart);

    @Insert
    long[] insertDish(RoomDishPart... roomDishParts);

    @Update
    int updateDish(RoomDishPart roomDishPart);

    @Update
    int updateDish(RoomDishPart... roomDishParts);

    @Delete
    int deleteDish(RoomDishPart roomDishPart);

    @Delete
    int deleteDish(RoomDishPart... roomDishParts);

    @Query("SELECT * FROM dish")
    List<RoomDishPart> getAllDishes();
}
