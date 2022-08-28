package apodviewer.apod;

import apodviewer.apod.model.NasaApod;

import java.util.List;

public interface ApodClient {
    public NasaApod getLatestApod();

    public NasaApod getApod(String date);

    public List<NasaApod> searchApod(String searchString);

    public List<NasaApod> getApodFrom(String startDate);
}
