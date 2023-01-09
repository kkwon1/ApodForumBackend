package apodviewer.comments;

import apodviewer.comments.db.CommentsClient;
import apodviewer.comments.model.CommentPointerNode;
import apodviewer.comments.model.CommentTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentsController {
    private static final String COMMENTS_PATH = "/comments";

    @Autowired
    private CommentsClient commentsClient;

    @PostMapping(path = COMMENTS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addComment(@RequestBody CommentPointerNode commentNode) {
        return commentsClient.addComment(commentNode.getCommentId(), commentNode.getComment(), commentNode.getAuthor());
    }

    @GetMapping(path = COMMENTS_PATH, params = {"post_id"})
    public CommentTreeNode getAllComments(@RequestParam String post_id) {
        return commentsClient.getAllComments(post_id);
    }
}
