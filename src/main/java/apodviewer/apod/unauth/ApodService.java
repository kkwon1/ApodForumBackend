package apodviewer.apod.unauth;


import apodviewer.apod.unauth.model.NasaApod;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

// TODO: Add proper error handling
// TODO :Add input validation
@Service
public class ApodService {
    private static final String NASA_APOD_BASE_URL = "https://api.nasa.gov/";

    Dotenv dotenv = Dotenv.load();
    private final String NASA_API_KEY = dotenv.get("NASA_API_KEY");

    private final ApodInterface service;

    public ApodService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NASA_APOD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApodInterface.class);
    }

    public NasaApod getApod() throws IOException {
        Call<NasaApod> retrofitCall = service.getApod(NASA_API_KEY);
        Response<NasaApod> response = retrofitCall.execute();

        return response.body();
    }

    public List<NasaApod> getRandomApods() throws IOException {
        Call<List<NasaApod>> call = service.getRandomApods(NASA_API_KEY, 10);
        Response<List<NasaApod>> response = call.execute();

        return response.body();
    }
}
