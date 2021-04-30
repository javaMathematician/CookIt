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

public final class Deserializer {
    private static final Gson gson = new Gson();

    public static List<Dish> deserializeDishes(InputStream stream) throws IOException {
        return Arrays.asList(gson.fromJson(IOUtil.toString(stream), Dish[].class));
    }

    public static Map<String, List<String>> deserializeDishToRawIngredients(InputStream stream) throws IOException {
        return gson.fromJson(IOUtil.toString(stream), new TypeToken<Map<String, List<String>>>() {
        }.getType());
    }

    public static Map<String, String> deserializeStringToString(InputStream stream) throws IOException {
        return gson.fromJson(IOUtil.toString(stream), new TypeToken<Map<String, String>>() {
        }.getType());
    }
}
