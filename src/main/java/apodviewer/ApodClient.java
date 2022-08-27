package apodviewer;

import apodviewer.model.Apod;

public interface ApodClient {
    Apod getRandomApod();
}
