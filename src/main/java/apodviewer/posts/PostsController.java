package apodviewer.posts;

import apodviewer.apod.client.ApodClient;
import apodviewer.apod.model.NasaApod;
import apodviewer.comments.client.CommentsClient;
import apodviewer.comments.model.CommentTree;
import apodviewer.posts.client.PostUpvoteClient;
import apodviewer.posts.model.UpvotePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostsController {
    private static final String POST_PATH = "/post";

    @Autowired
    private ApodClient apodClient;

    @Autowired
    private CommentsClient commentsClient;

    @Autowired
    private PostUpvoteClient upvoteClient;

    @GetMapping(value = POST_PATH, params = {"post_id"})
    public ApodPost getPost(@RequestParam String post_id) {
        NasaApod nasaApod = apodClient.getApod(post_id);
        CommentTree rootComment = commentsClient.getPostComments(post_id);

        return ApodPost.builder()
                .nasaApod(nasaApod)
                .comments(rootComment)
                .build();
    }

    @PostMapping(path = POST_PATH + "/upvote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void upvote(@RequestBody UpvotePostRequest upvotePostRequest) {
        System.out.println("Received Request");
        upvoteClient.upvotePost(upvotePostRequest);
        apodClient.upvotePost(upvotePostRequest.getPostId());
    }
}
