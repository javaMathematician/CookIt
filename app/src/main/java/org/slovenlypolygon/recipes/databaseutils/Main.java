package org.slovenlypolygon.recipes.databaseutils;

import org.slovenlypolygon.recipes.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.databaseutils.Dish;
import org.slovenlypolygon.recipes.databaseutils.DishFilterBuilder;
import org.slovenlypolygon.recipes.exceptions.UnknownFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, UnknownFileException {
        DishFilterBuilder builder = new DishFilterBuilder(new Deserializer().deserialize("C:\\Users\\serega\\PycharmProjects\\food\\all_dishes.json"));

        builder.setName("Салат");
        builder.setCategories(new ArrayList<>(Collections.singletonList("греч")));
        builder.setRecipeIngredients(new ArrayList<>(Arrays.asList("ЛУК", "пЕрЕц")));

        for (Dish dish : builder.getMatchingList()) {
            System.out.println(dish + "\n");
        }
    }
}
