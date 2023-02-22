package apodviewer.posts.db;

import apodviewer.posts.model.Upvote;

import java.util.List;

public interface PostUpvoteDao {
    void upvotePost(Upvote upvote);

    List<String> getUpvotedPostIds(String userSub);
}
