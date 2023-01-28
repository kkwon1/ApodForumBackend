package apodviewer.posts.model;

import lombok.Value;

@Value
public class UpvotePostRequest {
    String postId;
    String userSub;
}
