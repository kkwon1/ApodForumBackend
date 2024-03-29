package apodviewer.comments;

import apodviewer.apod.client.ApodClient;
import apodviewer.comments.client.CommentsClient;
import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;
import apodviewer.comments.model.DeleteCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentsController {
    private static final String COMMENT_PATH = "/comment";

    @Autowired
    private CommentsClient commentsClient;

    @Autowired
    private ApodClient apodClient;

    @PostMapping(path = COMMENT_PATH + "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Comment addComment(@RequestBody AddCommentRequest addCommentRequest) {
        Comment comment = commentsClient.addComment(addCommentRequest);
        apodClient.addCommentToPost(addCommentRequest.getPostId());
        return comment;
    }

    @PostMapping(path = COMMENT_PATH + "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest) {
        return commentsClient.softDeleteComment(deleteCommentRequest);
    }

    @GetMapping(path = COMMENT_PATH, params = {"post_id"})
    public CommentTree getPostComments(@RequestParam String post_id) {
        return commentsClient.getPostComments(post_id);
    }
}
