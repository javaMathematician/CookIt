package org.slovenlypolygon.recipes.backend.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Joiner;

import org.slovenlypolygon.recipes.backend.mainobjects.Component;
import org.slovenlypolygon.recipes.backend.mainobjects.ComponentType;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.Step;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishComponentDAO {
    private final SQLiteDatabase database;

    public DishComponentDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public Observable<Dish> getDishesByIDs(Set<Integer> dishesIDs) {
        return Observable.create(emitter -> {
            String joinedIDs = Joiner.on(", ").join(dishesIDs);
            String query = "SELECT * FROM dish WHERE dishID IN (" + joinedIDs + ")";

            try (Cursor cursor = database.rawQuery(query, null)) {
                while (cursor.moveToNext()) {
                    Dish dish = getDishFromCursor(cursor);

                    fillCleanIngredients(dish);
                    emitter.onNext(dish);
                }
            }

            emitter.onComplete();
        });
    }

    private Dish getDishFromCursor(Cursor cursor) {
        int dishID = cursor.getInt(cursor.getColumnIndex("dishID"));
        String dishName = cursor.getString(cursor.getColumnIndex("dishName"));
        String dishImageURL = cursor.getString(cursor.getColumnIndex("dishImageURL"));
        String dishURL = cursor.getString(cursor.getColumnIndex("dishURL"));

        return new Dish(dishID, dishName, dishImageURL, dishURL);
    }

    public Observable<Dish> getDishesFromComponentIDs(Set<Integer> componentIDs) {
        if (componentIDs.isEmpty()) {
            return getAllDishes();
        }

        return Observable.create(emitter -> {
            String joinedIDs = Joiner.on(", ").join(componentIDs);

            String query = "SELECT *, count(dish.dishID) as counter FROM dish, component, dishComponentCrossReference as ref " +
                    "WHERE component.componentID IN (" + joinedIDs + ") " +
                    "AND ref.componentID = component.componentID " +
                    "AND ref.dishID = dish.dishID " +
                    "GROUP BY dish.dishName " +
                    "ORDER BY counter DESC";

            try (Cursor cursor = database.rawQuery(query, null)) {
                while (cursor.moveToNext()) {
                    Dish dish = getDishFromCursor(cursor);

                    fillCleanIngredients(dish);
                    emitter.onNext(dish);
                }
            }

            emitter.onComplete();
        });
    }

    private void fillCleanIngredients(Dish dish) {
        String query = "SELECT component.componentID, componentName, componentImageURL FROM component " +
                "JOIN dishComponentCrossReference ON dishComponentCrossReference.componentID = component.componentID " +
                "JOIN dish ON dishComponentCrossReference.dishID = dish.dishID " +
                "WHERE dish.dishID = " + dish.getId() + " AND qIsIngredient = 1";

        try (Cursor cursor = database.rawQuery(query, null)) {
            Set<Component> components = new TreeSet<>(Comparator.comparing(Component::getName));

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("componentID"));
                String name = cursor.getString(cursor.getColumnIndex("componentName"));
                String imageURL = cursor.getString(cursor.getColumnIndex("componentImageURL"));

                components.add(new Component(id, ComponentType.INGREDIENT, name, imageURL));
            }

            dish.setCleanComponents(components);
        }
    }

    private void fillDirtyIngredients(Dish dish) {
        String query = "SELECT content FROM rawIngredient WHERE dishID = " + dish.getId();
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
                    Dish dish = getDishFromCursor(cursor);

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

            try (Cursor cursor = database.rawQuery("SELECT DISTINCT * FROM component WHERE qIsIngredient = " + booleState, null)) {
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
        String query = "SELECT stepText, stepImageUrl FROM step WHERE dishID = " + dish.getId() + " ORDER BY localOrder";

        try (Cursor cursor = database.rawQuery(query, null)) {
            List<Step> steps = new ArrayList<>();

            while (cursor.moveToNext()) {
                String text = cursor.getString(cursor.getColumnIndex("stepText"));
                String stepImageUrl = cursor.getString(cursor.getColumnIndex("stepImageURL"));

                Step step = new Step(text);
                if (stepImageUrl != null && !stepImageUrl.isEmpty()) {
                    step.setImageURL(stepImageUrl);
                }

                steps.add(step);
            }

            dish.setSteps(steps);
        }
    }

    public Dish getRichDish(Dish dish) {
        Dish clone = new Dish(dish);

        fillDirtyIngredients(clone);
        fillSteps(clone);
        return clone;
    }

    public String getCleanComponentNameByID(int componentID) {
        String query = "SELECT componentName FROM component WHERE componentID = " + componentID;

        try (Cursor cursor = database.rawQuery(query, null)) {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
    }

    public void addToFavorites(Dish dish) {
        database.execSQL("INSERT INTO favorites (dishID) VALUES (" + dish.getId() + ")");
    }

    public void removeFromFavorites(Dish dish) {
        database.execSQL("DELETE FROM favorites WHERE dishID = " + dish.getId());
    }

    public Set<Integer> getFavoritesIDs() {
        String query = "SELECT dishID FROM favorites";
        Set<Integer> ids = new HashSet<>();

        try (Cursor cursor = database.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(0));
            }
        }

        return ids;
    }

    public boolean containsFavorites(Dish dish) {
        String query = "SELECT count(*) FROM favorites WHERE dishID = " + dish.getId();

        try (Cursor cursor = database.rawQuery(query, null)) {
            cursor.moveToFirst();
            return cursor.getInt(0) > 0;
        }
    }

    public Observable<Dish> getRecommendedDishes() {
        Set<Integer> dishesIDs = getFavoritesIDs();

        if (dishesIDs.isEmpty()) {
            return getAllDishes();
        }

        return Observable.create(emitter -> {
            Set<Integer> componentsIDs = new HashSet<>();
            String joinedDishesIDs = Joiner.on(", ").join(dishesIDs);

            String queryForComponents = "SELECT componentID FROM dishComponentCrossReference " +
                    "WHERE dishID IN (" + joinedDishesIDs + ")";

            try (Cursor cursor = database.rawQuery(queryForComponents, null)) {
                while (cursor.moveToNext()) {
                    componentsIDs.add(cursor.getInt(0));
                }
            }

            String joinedComponentsIDs = Joiner.on(", ").join(componentsIDs);
            Set<Integer> recommendedCategoriesIDs = new HashSet<>();

            String query = "SELECT componentID FROM component " +
                    "WHERE qIsIngredient = 0 AND componentID IN (" + joinedComponentsIDs + ")";

            try (Cursor cursor = database.rawQuery(query, null)) {
                while (cursor.moveToNext()) {
                    recommendedCategoriesIDs.add(cursor.getInt(0));
                }
            }

            getDishesFromComponentIDs(recommendedCategoriesIDs)
                    .subscribeOn(Schedulers.newThread())
                    .skip(dishesIDs.size())
                    .take(10)
                    .subscribe(emitter::onNext, Throwable::printStackTrace);
        });
    }
}
