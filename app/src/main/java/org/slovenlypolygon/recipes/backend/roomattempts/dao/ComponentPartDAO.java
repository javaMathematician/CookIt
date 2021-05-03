package org.slovenlypolygon.recipes.backend.roomattempts.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.slovenlypolygon.recipes.backend.roomattempts.parts.RoomComponentPart;

import java.util.List;

@Dao
public interface ComponentPartDAO {
    @Insert
    long insertComponent(RoomComponentPart component);

    @Insert
    long[] insertComponent(RoomComponentPart... component);

    @Update
    int updateComponent(RoomComponentPart component);

    @Update
    int updateComponent(RoomComponentPart... component);

    @Delete
    int deleteComponent(RoomComponentPart component);

    @Delete
    int deleteComponent(RoomComponentPart... component);

    @Query("SELECT * FROM component")
    List<RoomComponentPart> getAllComponents();

    @Query("SELECT * FROM component WHERE id = :dishId")
    List<RoomComponentPart> getDishComponents(final int dishId);
}
