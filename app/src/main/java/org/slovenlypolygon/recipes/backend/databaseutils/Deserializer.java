package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtil;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Deserializer {
    public static List<Dish> deserializeDishes(InputStream stream) throws IOException {
        return Arrays.asList(new Gson().fromJson(IOUtil.toString(stream), Dish[].class));
    }
}
