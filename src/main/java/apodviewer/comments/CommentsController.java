package apodviewer.comments;

import apodviewer.apod.model.NasaApod;
import apodviewer.comments.db.CommentsClient;
import apodviewer.comments.model.CommentNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentsController {
    private static final String COMMENTS_PATH = "/comments";

    @Autowired
    private CommentsClient commentsClient;

    @PostMapping(path = COMMENTS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addComment(@RequestBody CommentNode commentNode) {
        commentsClient.addComment(commentNode.getCommentId(), commentNode.getComment());
    }
}
