package org.slovenlypolygon.recipes.backend.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.util.Set;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class DAOFacade {
    private final SQLiteDatabase database;

    public DAOFacade(SQLiteDatabase database) {
        this.database = database;
    }

    public Observable<Dish> getDishesFromComponentIDs(Set<Integer> componentIDs) {
        PublishSubject<Dish> publishSubject = PublishSubject.create();

        for (int iterate : componentIDs) { // входим в поле одного ингредиента
            String query = "SELECT * FROM component " +
                    "JOIN dishComponentCrossReference " +
                    "ON component.componentID = dishComponentCrossReference.componentID " +
                    "JOIN dish ON dish.dishID = dishComponentCrossReference.dishID " +
                    "WHERE component.componentID = " + iterate; // все блюда на текущий ингредиент

            try (Cursor cursor = database.rawQuery(query, null)) {
                while (cursor.moveToNext()) {
                    int dishID = cursor.getInt(cursor.getColumnIndex("dishID"));
                    String dishName = cursor.getString(cursor.getColumnIndex("dishName"));
                    String dishImageURL = cursor.getString(cursor.getColumnIndex("dishImageURL"));
                    String dishURL = cursor.getString(cursor.getColumnIndex("dishURL"));

                    publishSubject.onNext(new Dish(dishID, dishName, dishImageURL, dishURL));
                    System.out.println(dishName);
                }
            }
        }
/*
            cursor.moveToFirst();
            String componentName = cursor.getString(cursor.getColumnIndex("componentName"));
            boolean isIngredient = cursor.getInt(cursor.getColumnIndex("qIsIngredient")) != 0;
            String componentImageURL = cursor.getString(cursor.getColumnIndex("componentImageURL"));

            Component component = new Component(isIngredient ? ComponentType.INGREDIENT : ComponentType.CATEGORY, componentImageURL, componentName);
*/
        return publishSubject;
    }

    public Observable<Dish> getAllDishes() {
        PublishSubject<Dish> publishSubject = PublishSubject.create();

        try (Cursor cursor = database.rawQuery("SELECT * FROM dish", null)) {
            while (cursor.moveToNext()) {
                int dishID = cursor.getInt(cursor.getColumnIndex("dishID"));
                String dishName = cursor.getString(cursor.getColumnIndex("dishName"));
                String dishImageURL = cursor.getString(cursor.getColumnIndex("dishImageURL"));
                String dishURL = cursor.getString(cursor.getColumnIndex("dishURL"));

                publishSubject.onNext(new Dish(dishID, dishName, dishImageURL, dishURL));
            }
        }

        return publishSubject;
    }

    public Observable<Component> getComponentByType(ComponentType type) {
        int booleState = type == ComponentType.CATEGORY ? 0 : 1;
        PublishSubject<Component> publishSubject = PublishSubject.create();

        try (Cursor cursor = database.rawQuery("SELECT * FROM component WHERE qIsIngredient = " + booleState, null)) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("componentID"));
                String name = cursor.getString(cursor.getColumnIndex("componentName"));
                String imageURL = cursor.getString(cursor.getColumnIndex("componentImageURL"));

                publishSubject.onNext(new Component(id, type, name, imageURL));
            }
        }

        return publishSubject;
    }

    public Observable<Dish> fillShortIngredients(Set<Integer> dishIDs) {
        PublishSubject<Dish> publishSubject = PublishSubject.create();

        for (int dishID : dishIDs) {
            try (Cursor cursor = database.rawQuery("SELECT * FROM dish " +
                    "JOIN step ON step.dishID = dish.dishID " +
                    "WHERE dish.dishID = " + dishID, null)) {

                while (cursor.moveToNext()) {
                    break;
                }
            } // все шаги на текущее блюдо
        }

        return publishSubject;
    }
}
