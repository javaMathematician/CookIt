package org.slovenlypolygon.recipes.backend.computervision;

import android.graphics.Bitmap;
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

import io.reactivex.rxjava3.core.Single;

public class OCR {
    public static Single<Set<String>> parseImage(Bitmap bitmap) {
        return Single.create(emitter -> {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.ocr.space/parse/image").openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoOutput(true);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);

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
                String regex = "[а-я]{2,}|[nutela]{3,}";

                Set<String> parsedWords = new HashSet<>();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(parsedRaw);

                while (matcher.find()) {
                    parsedWords.add(parsedRaw.substring(matcher.start(), matcher.end()));
                }

                emitter.onSuccess(parsedWords);
            }
        });
    }
}