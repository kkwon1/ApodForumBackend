package apodviewer.posts;

import apodviewer.apod.client.ApodClient;
import apodviewer.apod.model.NasaApod;
import apodviewer.comments.client.CommentsClient;
import apodviewer.comments.model.CommentTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostsController {
    private static final String POST_PATH = "/post";

    @Autowired
    private ApodClient apodClient;

    @Autowired
    private CommentsClient commentsClient;

    @GetMapping(value = POST_PATH, params = {"post_id"})
    public ApodPost getPost(@RequestParam String post_id) {
        NasaApod nasaApod = apodClient.getApod(post_id);
        CommentTree rootComment = commentsClient.getPostComments(post_id);

        return ApodPost.builder()
                .nasaApod(nasaApod)
                .comments(rootComment)
                .build();
    }

}
