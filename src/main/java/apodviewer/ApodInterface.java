package apodviewer;

import apodviewer.model.NasaApod;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodInterface {
    @GET("/planetary/apod")
    public Call<NasaApod> getApod(@Query("api_key") String key);
}
