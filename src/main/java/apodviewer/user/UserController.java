package apodviewer.user;

import apodviewer.posts.client.PostUpvoteClient;
import apodviewer.user.model.GetUserRequest;
import apodviewer.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private static final String USER_PATH = "/user";

    @Autowired
    private PostUpvoteClient upvoteClient;

    @PostMapping(path = USER_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@RequestBody GetUserRequest getUserRequest) {
        // TODO: implement
        // return User likes
        // return User saves
        // return User setting/configuration (if any)
        List<String> upvotedPostIds = upvoteClient.getUpvotedPostIds(getUserRequest.getUserSub());
        return User.builder()
                .upvotedPostIds(upvotedPostIds)
                .build();
    }
}
