package apodviewer.comments.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * The data structure used to store comments within the DB.
 *
 * A single CommentPointerNode represents a single Comment document in the DB
 *
 * Each comment node will have its info and the parent node. This allows us to query based on the parent ID
 * and it will return all the children of given parent. This allows us to build the TreeNode Top Down.
 *
 * Refer to https://www.mongodb.com/docs/manual/tutorial/model-tree-structures-with-parent-references/
 */
@Builder
@Value
public class Comment {
    String commentId;
    String parentCommentId;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    String commentText;
    String author;
    Boolean isDeleted;
    Boolean isLeaf;
}
