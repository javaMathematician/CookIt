package org.slovenlypolygon.recipes;

import android.util.Log;
import org.slovenlypolygon.recipes.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.databaseutils.Dish;
import org.slovenlypolygon.recipes.databaseutils.DishFilterBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CardGenerator {
    private InputStream inputStream;

    public CardGenerator(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<Dish> main() {
        try {
            Deserializer deserializer = new Deserializer();
            DishFilterBuilder dishFilterBuilder = new DishFilterBuilder(deserializer.deserialize(inputStream));
            return dishFilterBuilder.getMatchingList();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
