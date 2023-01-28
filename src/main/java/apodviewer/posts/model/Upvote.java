package apodviewer.posts.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Upvote {
    String postId;
    String userSub;
    LocalDateTime dateTime;
}
