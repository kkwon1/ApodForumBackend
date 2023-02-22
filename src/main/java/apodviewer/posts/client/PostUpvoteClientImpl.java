package apodviewer.posts.client;

import apodviewer.posts.db.PostUpvoteDao;
import apodviewer.posts.model.Upvote;
import apodviewer.posts.model.UpvotePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostUpvoteClientImpl implements PostUpvoteClient {

    @Autowired
    private PostUpvoteDao postUpvoteDao;

    @Override
    public void upvotePost(UpvotePostRequest upvotePostRequest) {
        Upvote upvote = Upvote.builder()
                .postId(upvotePostRequest.getPostId())
                .userSub(upvotePostRequest.getUserSub())
                .dateTime(LocalDateTime.now())
                .build();

        postUpvoteDao.upvotePost(upvote);
    }

    @Override
    public List<String> getUpvotedPostIds(String userSub) {
        return postUpvoteDao.getUpvotedPostIds(userSub);
    }
}
