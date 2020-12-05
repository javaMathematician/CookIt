package org.slovenlypolygon.recipes.backend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReader {
    public static String readAll(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine;

        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine).append("\n");
        }

        return stringBuilder.toString();
    }

}
