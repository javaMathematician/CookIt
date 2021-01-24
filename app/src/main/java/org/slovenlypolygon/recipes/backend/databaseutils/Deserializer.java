package org.slovenlypolygon.recipes.backend.databaseutils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slovenlypolygon.recipes.backend.mainobjects.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Deserializer {
    private static String readAll(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine;

        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine).append("\n");
        }

        return stringBuilder.toString();
    }

    public static List<Dish> deserializeDish(InputStream stream) throws IOException {
        return Arrays.asList(new Gson().fromJson(readAll(stream), Dish[].class));
    }

    public static Map<String, String> deserializeMap(InputStream stream) throws IOException {
        return new Gson().fromJson(readAll(stream), new TypeToken<Map<String, String>>() {
        }.getType());
    }
}
