package apodviewer.posts;

import apodviewer.apod.model.NasaApod;
import apodviewer.comments.model.CommentTreeNode;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApodPost {
    NasaApod nasaApod;
    CommentTreeNode comments;
}
