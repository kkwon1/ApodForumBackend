package apodviewer.model;

import lombok.Value;

@Value
public class NasaApod {
    String copyright;
    String date;
    String explanation;
    String mediaType;
    String serviceVersion;
    String title;
    String url;
    String hdurl;
}
