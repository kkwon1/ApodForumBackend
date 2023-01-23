package apodviewer.comments;

import apodviewer.comments.db.CommentsClient;
import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.CommentPointerNode;
import apodviewer.comments.model.CommentTreeNode;
import apodviewer.comments.model.DeleteCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentsController {
    private static final String COMMENT_PATH = "/comment";

    @Autowired
    private CommentsClient commentsClient;

    @PostMapping(path = COMMENT_PATH + "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommentPointerNode addComment(@RequestBody AddCommentRequest addCommentRequest) {
        return commentsClient.addComment(addCommentRequest);
    }

    @PostMapping(path = COMMENT_PATH + "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest) {
        return commentsClient.deleteComment(deleteCommentRequest);
    }

    @GetMapping(path = COMMENT_PATH, params = {"post_id"})
    public CommentTreeNode getAllComments(@RequestParam String post_id) {
        return commentsClient.getAllComments(post_id);
    }
}
