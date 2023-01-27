package apodviewer.comments.client;

import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;
import apodviewer.comments.model.DeleteCommentRequest;

public interface CommentsClient {
    CommentTree getPostComments(String postId);

    Comment addComment(AddCommentRequest addCommentRequest);

    String softDeleteComment(DeleteCommentRequest deleteCommentRequest);

    void upvoteComment();
}
