package org.slovenlypolygon.recipes.billscanner;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

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
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.rxjava3.core.Observable;

public class OCR {
    public static Observable<Set<String>> parseImage(Bitmap bitmap) {
        return Observable.create(emitter -> {
            for (int degrees : Arrays.asList(0, 90, 180, 270)) {
                if (!emitter.isDisposed()) {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.ocr.space/parse/image").openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    connection.setDoOutput(true);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    BitmapUtils.scaleBitmap(BitmapUtils.rotate(bitmap, degrees)).compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

                    String base64 = "data:image/png;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    String encodedQuery = new Uri.Builder()
                            .appendQueryParameter("apikey", "e9525e2a8b88957")
                            .appendQueryParameter("language", "rus")
                            .appendQueryParameter("base64Image", base64)
                            .build().getEncodedQuery();

                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    writer.write(encodedQuery);

                    writer.flush();
                    writer.close();
                    outputStream.close();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String string = IOUtil.toString(reader);
                        Set<String> strings = new TreeSet<>();
                        Log.d("TAG", string);

                        JSONObject object = (JSONObject) new JSONObject(string).getJSONArray("ParsedResults").get(0);
                        String[] parsedRaw = object.getString("ParsedText").toLowerCase().split("\\r\\n");

                        Pattern pattern = Pattern.compile("[а-яnutela ]{3,}");

                        for (String item : parsedRaw) {
                            Matcher matcher = pattern.matcher(item);

                            if (matcher.find()) {
                                strings.add(item.substring(matcher.start(), matcher.end()));
                            }
                        }

                        emitter.onNext(strings);
                    }
                } else {
                    break;
                }
            }

            emitter.onComplete();
        });
    }
}