package org.slovenlypolygon.recipes.backend.roomattempts.parts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dish")
public class RoomDishPart {
    @PrimaryKey
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
