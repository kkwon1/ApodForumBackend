package apodviewer.apod.db;

import apodviewer.apod.model.NasaApod;

import java.util.List;

public interface ApodClient {
    NasaApod getLatestApod();

    NasaApod getApod(String date);

    List<NasaApod> searchApod(String searchString);

    List<NasaApod> getApodFrom(String startDate);

    List<NasaApod> getApodFromTo(String startDate, String endDate);

    List<NasaApod> getRandomApods(Integer count);
}
