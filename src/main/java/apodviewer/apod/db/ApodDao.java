package apodviewer.apod.db;

import apodviewer.apod.model.NasaApod;

import java.util.List;

public interface ApodDao {

    NasaApod getApod(String postId);

    List<NasaApod> getAllApod();

    List<NasaApod> getApodFromTo(String startDate, String endDate);

    NasaApod getRandomApod();

    List<NasaApod> getRandomApods(Integer count);

    void incrementCommentCount(String postId);
}
