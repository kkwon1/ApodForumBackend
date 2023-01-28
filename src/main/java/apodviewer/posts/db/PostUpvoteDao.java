package apodviewer.posts.db;

import apodviewer.posts.model.Upvote;

public interface PostUpvoteDao {
    public void upvotePost(Upvote upvote);
}
