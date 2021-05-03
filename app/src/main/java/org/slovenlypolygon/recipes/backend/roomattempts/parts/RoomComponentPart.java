package org.slovenlypolygon.recipes.backend.roomattempts.parts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "component")
public class RoomComponentPart {
    @PrimaryKey
    private Integer id;
    private String name = "null";

    @ColumnInfo(name = "is_ingredient")
    private Integer isIngredient;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsIngredient() {
        return isIngredient;
    }

    public void setIsIngredient(Integer isIngredient) {
        this.isIngredient = isIngredient;
    }
}
