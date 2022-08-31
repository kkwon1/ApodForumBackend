package apodviewer.comments.db;

import apodviewer.comments.model.CommentNode;

import java.util.List;

public interface CommentsClient {
    List<CommentNode> getComments(String postId);

    void addComment(String parentCommentId, String commentText);
}
