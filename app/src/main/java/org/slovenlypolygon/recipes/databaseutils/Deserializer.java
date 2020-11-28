package org.slovenlypolygon.recipes.databaseutils;

import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Deserializer {
    private String readAll(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine;

        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine).append("\n");
        }

        return stringBuilder.toString();
    }

    public List<Dish> deserialize(InputStream stream) throws IOException {
        return Arrays.asList(new Gson().fromJson(readAll(stream), Dish[].class));
    }

    public List<Dish> deserialize(String path) throws IOException {
        return Arrays.asList(new Gson().fromJson(readAll(new FileInputStream(path)), Dish[].class));
    }

    public List<Dish> deserialize(File file) throws IOException {
        return Arrays.asList(new Gson().fromJson(readAll(new FileInputStream(file)), Dish[].class));
    }
}
