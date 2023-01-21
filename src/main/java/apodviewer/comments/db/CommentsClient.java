package apodviewer.comments.db;

import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.CommentTreeNode;

public interface CommentsClient {
    CommentTreeNode getAllComments(String postId);

    String addComment(AddCommentRequest addCommentRequest);
}
