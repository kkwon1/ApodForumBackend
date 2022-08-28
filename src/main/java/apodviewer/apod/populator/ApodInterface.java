package apodviewer.apod.populator;

import apodviewer.apod.model.NasaApod;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

/**
 * Official docs found in https://github.com/nasa/apod-api
 */
public interface ApodInterface {
    @GET("/planetary/apod")
    public Call<NasaApod> getApod(@Query("api_key") String key);

    @GET("/planetary/apod")
    public Call<List<NasaApod>> getRandomApods(@Query("api_key") String key,
                                               @Query("count") Integer num);

    @GET("/planetary/apod")
    public Call<NasaApod> getApodForDate(@Query("api_key") String key,
                                         @Query("date") String date);

    @GET("/planetary/apod")
    public Call<List<NasaApod>> getApodFrom(@Query("api_key") String key,
                                            @Query("start_date") String startDate);

    @GET("/planetary/apod")
    public Call<List<NasaApod>> getApodFromTo(@Query("start_Date") String startDate,
                                              @Query("end_date") String endDate);
}
