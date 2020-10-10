package org.slovenlypolygon.recipes.databaseutils;

import com.google.gson.Gson;

import org.slovenlypolygon.recipes.exceptions.UnknownFileException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class Deserializer {
    private String readAll(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine).append("\n");
        }

        return stringBuilder.toString();
    }

    public List<Dish> deserialize(String filePath) throws IOException, ClassNotFoundException, UnknownFileException {
        if (filePath.endsWith(".ser")) {
            return (List<Dish>) new ObjectInputStream(new FileInputStream(filePath)).readObject();
        } else if (filePath.endsWith(".json")) {
            return Arrays.asList(new Gson().fromJson(readAll(filePath), Dish[].class));
        }

        throw new UnknownFileException("Unknown file " + filePath);
    }
}
