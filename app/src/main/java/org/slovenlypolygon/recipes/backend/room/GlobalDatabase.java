package org.slovenlypolygon.recipes.backend.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.slovenlypolygon.recipes.backend.room.objects.Component;
import org.slovenlypolygon.recipes.backend.room.objects.Dish;

@Database(entities = {Component.class, DishWithIngredients.class, Dish.class}, version = 1)
public abstract class GlobalDatabase extends RoomDatabase {
    public abstract DAO getDAO();
}
