package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slovenlypolygon.recipes.backend.utils.FileReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Deserializer {
    public static List<Dish> deserializeDish(InputStream stream) throws IOException {
        return Arrays.asList(new Gson().fromJson(FileReader.readAll(stream), Dish[].class));
    }

    public static Map<String, String> deserializeMap(InputStream stream) throws IOException {
        return new Gson().fromJson(FileReader.readAll(stream), new TypeToken<Map<String, String>>() {}.getType());
    }
}
