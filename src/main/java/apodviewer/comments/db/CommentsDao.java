package apodviewer.comments.db;

import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;
import apodviewer.comments.model.DeleteCommentRequest;

public interface CommentsDao {
    CommentTree getPostComments(String postId);

    String deleteComment(String commentId);

    void addComment(Comment comment);
}
