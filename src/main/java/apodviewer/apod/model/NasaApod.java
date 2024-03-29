package apodviewer.apod.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NasaApod {
    String copyright;
    String postId;
    String date;
    String explanation;
    String mediaType;
    String serviceVersion;
    String title;
    String url;
    String hdurl;
    int upvoteCount;
    int saveCount;
    int commentCount;
}
