package org.slovenlypolygon.recipes.backend.computervision;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;

import org.apache.commons.io.IOUtil;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.rxjava3.core.Observable;

public class OCR {
    public static Observable<Set<String>> parseImage(Bitmap bitmap) {
        return Observable.create(emitter -> {
            for (int degrees : new int[]{0, 90, 180, 270}) {
                HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.ocr.space/parse/image").openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setDoOutput(true);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                scaleBitmap(rotate(bitmap, degrees)).compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                String string = "data:image/png;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                String encodedQuery = new Uri.Builder()
                        .appendQueryParameter("apikey", "e9525e2a8b88957")
                        .appendQueryParameter("language", "rus")
                        .appendQueryParameter("base64Image", string)
                        .build().getEncodedQuery();

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                writer.write(encodedQuery);

                writer.flush();
                writer.close();
                outputStream.close();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    JSONObject object = (JSONObject) new JSONObject(IOUtil.toString(reader)).getJSONArray("ParsedResults").get(0);
                    String parsedRaw = object.getString("ParsedText").toLowerCase();
                    String regex = "[а-я]{4,}|[nutela]{4,}";

                    Set<String> parsedWords = new HashSet<>();
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(parsedRaw);

                    while (matcher.find()) {
                        parsedWords.add(parsedRaw.substring(matcher.start(), matcher.end()));
                    }

                    emitter.onNext(parsedWords);
                }
            }
        });
    }

    private static Bitmap scaleBitmap(Bitmap input) {
        final int currentWidth = input.getWidth();
        final int currentHeight = input.getHeight();
        final int currentPixels = currentWidth * currentHeight;

        final long maxPixels = 1024 * 1024 * 4;

        if (currentPixels <= maxPixels) {
            return input;
        }

        final double scaleFactor = Math.sqrt(maxPixels / (double) currentPixels);
        final int newWidthPx = (int) Math.floor(currentWidth * scaleFactor);
        final int newHeightPx = (int) Math.floor(currentHeight * scaleFactor);

        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true);
    }

    private static Bitmap rotate(Bitmap source, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }
}