package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtil;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Deserializer {

    public static List<Dish> deserializeDishes(InputStream stream) throws IOException {
        return Arrays.asList(new Gson().fromJson(IOUtil.toString(stream), Dish[].class));
    }

    public static Map<String, String> deserializeMap(InputStream stream) throws IOException {
        return new Gson().fromJson(IOUtil.toString(stream), new TypeToken<Map<String, String>>() {
        }.getType());
    }
}
