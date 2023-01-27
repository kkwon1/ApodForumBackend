package apodviewer.comments.client;

import apodviewer.comments.db.CommentsDao;
import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;
import apodviewer.comments.model.DeleteCommentRequest;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CommentsClientImpl implements CommentsClient {

    @Autowired
    private CommentsDao commentsDao;

    // TODO: Implement cache
    // TODO consider optimizing comment storage for faster retrieval

    @Override
    public CommentTree getPostComments(String postId) {
        return commentsDao.getPostComments(postId);
    }

    @Override
    public Comment addComment(AddCommentRequest addCommentRequest) {
        String newCommentId = UUID.randomUUID().toString();
        Comment newComment = Comment.builder()
                .commentId(newCommentId)
                .commentText(addCommentRequest.getComment())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .parentCommentId(addCommentRequest.getParentCommentId())
                .author(addCommentRequest.getAuthor())
                .isDeleted(false)
                .build();


        commentsDao.addComment(newComment);
        return newComment;
    }

    @Override
    public String softDeleteComment(DeleteCommentRequest deleteCommentRequest) {
        return commentsDao.deleteComment(deleteCommentRequest.getCommentId());
    }

    @Override
    public void upvoteComment() {
        throw new NotImplementedException("Not yet implemented");
    }
}
