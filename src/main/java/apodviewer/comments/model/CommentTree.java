package apodviewer.comments.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A data structure that will represent the comments on a post as a single tree.
 * Each node will have children cascading down. Any client side app will be able to render the comments naturally
 */
@Builder
@Data
public class CommentTree {
    String commentId;
    List<CommentTree> children;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    String comment;
    String author;
    Boolean isDeleted;
    Boolean isLeaf;
}
