package org.slovenlypolygon.recipes.backend.computervision;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OCRService {
    @POST("parse/image?language=rus")
    Single<String> parseImage(
            @Query("apikey") String apiKey,
            @Query("base64Image") String base64Image
    );
}
