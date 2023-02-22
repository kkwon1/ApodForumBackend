package apodviewer.posts.db;

import apodviewer.posts.model.Upvote;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MongoPostUpvoteConverter {
    public Document convertUpvoteToDocument(Upvote upvote) {
        return new Document()
                .append("postId", upvote.getPostId())
                .append("userSub", upvote.getUserSub())
                .append("timestamp", upvote.getDateTime());
    }

    public Upvote convertDocumentToUpvote(Document document) {
        return Upvote.builder()
                .postId(document.getString("postId"))
                .userSub(document.getString("userSub"))
                .dateTime(convertToLocalDateTimeViaInstant(document.getDate("timestamp")))
                .build();
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
