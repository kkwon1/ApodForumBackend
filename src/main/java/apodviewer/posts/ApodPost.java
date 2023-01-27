package apodviewer.posts;

import apodviewer.apod.model.NasaApod;
import apodviewer.comments.model.CommentTree;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApodPost {
    NasaApod nasaApod;
    CommentTree comments;
}
