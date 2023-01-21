package apodviewer.comments.model;

import lombok.Value;

@Value
public class AddCommentRequest {
    String parentCommentId;
    String comment;
    String author;
}
