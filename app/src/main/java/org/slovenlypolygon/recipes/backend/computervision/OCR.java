package org.slovenlypolygon.recipes.backend.computervision;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OCR {
    public static void getTextFromImage(Bitmap image) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ocr.space/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        OCRService service = retrofit.create(OCRService.class);

        service.parseImage("e9525e2a8b88957", "data:image/png;base64," + getBase64(image))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private static String getBase64(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
