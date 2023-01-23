package apodviewer.comments.model;

import lombok.Value;

@Value
public class AddCommentRequest {
    String parentCommentId;
    String postId;
    String comment;
    String author;
}
