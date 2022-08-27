package apodviewer;


import apodviewer.model.NasaApod;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

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

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody().toString());
        }

        return response.body();
    }
}
