package apodviewer.comments.model;

import lombok.Value;

@Value
public class DeleteCommentRequest {
    String commentId;
    String author;
}
