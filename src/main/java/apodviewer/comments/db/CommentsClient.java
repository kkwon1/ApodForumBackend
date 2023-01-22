package apodviewer.comments.db;

import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.CommentTreeNode;
import apodviewer.comments.model.DeleteCommentRequest;

public interface CommentsClient {
    CommentTreeNode getAllComments(String postId);
    String deleteComment(DeleteCommentRequest deleteCommentRequest);

    String addComment(AddCommentRequest addCommentRequest);
}
