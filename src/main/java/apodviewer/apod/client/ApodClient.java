package apodviewer.apod.client;

import apodviewer.apod.model.NasaApod;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ApodClient {
    NasaApod getRandomApod();

    List<NasaApod> getApodPage(String offset, String limit);

    NasaApod getApod(String postId);

    List<NasaApod> searchApod(String searchString);

    List<NasaApod> getRandomApods(Integer count);

    void addCommentToPost(String postId);
}
