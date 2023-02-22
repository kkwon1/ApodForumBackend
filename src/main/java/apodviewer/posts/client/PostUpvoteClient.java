package apodviewer.posts.client;

import apodviewer.posts.model.UpvotePostRequest;

import java.util.List;

public interface PostUpvoteClient {
    void upvotePost(UpvotePostRequest upvotePostRequest);

    List<String> getUpvotedPostIds(String userSub);
}
