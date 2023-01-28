package apodviewer.posts.db;

import apodviewer.posts.model.Upvote;
import org.bson.Document;

public class MongoPostUpvoteConverter {
    public Document convertUpvoteToDocument(Upvote upvote) {
        return new Document()
                .append("postId", upvote.getPostId())
                .append("userSub", upvote.getUserSub())
                .append("timestamp", upvote.getDateTime());
    }
}
