package apodviewer.posts.client;

import apodviewer.posts.model.UpvotePostRequest;

public interface PostUpvoteClient {
    void upvotePost(UpvotePostRequest upvotePostRequest);
}
