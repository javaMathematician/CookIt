package org.slovenlypolygon.recipes.backend.backendcards;

import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;
import org.slovenlypolygon.recipes.backend.databaseutils.DishFilterBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Plug {
    private final InputStream inputStream;

    public Plug(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<Dish> getAll() throws IOException {
        Deserializer deserializer = new Deserializer();
        DishFilterBuilder dishFilterBuilder = new DishFilterBuilder(deserializer.deserializeDish(inputStream));

        return dishFilterBuilder.getMatchingList();
    }
}
