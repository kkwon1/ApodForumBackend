package apodviewer.comments.db;

import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;

public interface CommentsDao {
    CommentTree getPostComments(String postId);

    String deleteComment(String commentId);

    void addComment(Comment comment);
}
