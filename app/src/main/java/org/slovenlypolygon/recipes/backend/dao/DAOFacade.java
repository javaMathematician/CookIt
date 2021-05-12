package org.slovenlypolygon.recipes.backend.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Step;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.rxjava3.core.Observable;

public class DAOFacade {
    private final SQLiteDatabase database;

    public DAOFacade(SQLiteDatabase database) {
        this.database = database;
    }

    public Observable<Dish> getDishesFromComponentIDs(Set<Integer> componentIDs) {
        if (componentIDs.isEmpty()) {
            return this.getAllDishes();
        }

        return Observable.create(emitter -> {
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

                        Dish dish = new Dish(dishID, dishName, dishImageURL, dishURL);

                        fillCleanIngredients(dish);
                        emitter.onNext(dish);
                    }
                }
            }

            emitter.onComplete();
        });
    }

    private void fillCleanIngredients(Dish dish) {
        String query = "SELECT component.componentID, componentName, componentImageURL FROM component " +
                "JOIN dishComponentCrossReference ON dishComponentCrossReference.componentID = component.componentID " +
                "JOIN dish ON dishComponentCrossReference.dishID = dish.dishID " +
                "WHERE dish.dishID = 1 AND qIsIngredient = 1";

        try (Cursor cursor = database.rawQuery(query, null)) {
            Set<Component> components = new TreeSet<>(Comparator.comparing(Component::getName));

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String imageURL = cursor.getString(cursor.getColumnIndex("componentImageURL"));

                components.add(new Component(id, ComponentType.INGREDIENT, name, imageURL));
            }

            dish.setCleanComponents(components);
        }
    }

    private void fillDirtyIngredients(Dish dish) {
        String query = "SELECT content from rawIngredient where dishID = " + dish.getId();
        Set<String> dirtyIngredients = new TreeSet<>(Comparator.comparing(String::length));

        try (Cursor cursor = database.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                dirtyIngredients.add(cursor.getString(cursor.getColumnIndex("content")));
            }
        }

        dish.setDirtyIngredients(dirtyIngredients);
    }

    public Observable<Dish> getAllDishes() {
        return Observable.create(emitter -> {
            try (Cursor cursor = database.rawQuery("SELECT * FROM dish", null)) {
                while (cursor.moveToNext()) {
                    int dishID = cursor.getInt(cursor.getColumnIndex("dishID"));
                    String dishName = cursor.getString(cursor.getColumnIndex("dishName"));
                    String dishImageURL = cursor.getString(cursor.getColumnIndex("dishImageURL"));
                    String dishURL = cursor.getString(cursor.getColumnIndex("dishURL"));

                    Dish dish = new Dish(dishID, dishName, dishImageURL, dishURL);

                    fillCleanIngredients(dish);
                    emitter.onNext(dish);
                }
            }

            emitter.onComplete();
        });
    }

    public Observable<Component> getComponentByType(ComponentType type) {
        return Observable.create(emitter -> {
            int booleState = type == ComponentType.CATEGORY ? 0 : 1;

            try (Cursor cursor = database.rawQuery("SELECT * FROM component WHERE qIsIngredient = " + booleState, null)) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("componentID"));
                    String name = cursor.getString(cursor.getColumnIndex("componentName"));
                    String imageURL = cursor.getString(cursor.getColumnIndex("componentImageURL"));

                    emitter.onNext(new Component(id, type, name, imageURL));
                }
            }

            emitter.onComplete();
        });
    }

    private void fillSteps(Dish dish) {
        String query = "SELECT stepText, stepImageUrl FROM step WHERE dishID = " + dish.getId();

        try (Cursor cursor = database.rawQuery(query, null)) {
            List<Step> steps = new ArrayList<>();

            while (cursor.moveToNext()) {
                String text = cursor.getString(cursor.getColumnIndex("stepText"));
                String stepImageUrl = cursor.getString(cursor.getColumnIndex("stepImageUrl"));

                Step step = new Step(text);
                if (stepImageUrl != null && !stepImageUrl.isEmpty()) {
                    step.setImageURL(stepImageUrl);
                }

                steps.add(step);
            }

            dish.setSteps(steps);
        }
    }
}
