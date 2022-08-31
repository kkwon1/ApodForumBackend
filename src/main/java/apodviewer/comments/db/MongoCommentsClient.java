package apodviewer.comments.db;

import apodviewer.apod.model.NasaApod;
import apodviewer.comments.model.CommentNode;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MongoCommentsClient implements CommentsClient {

    @Autowired
    @Qualifier("commentsCollection")
    private MongoCollection<Document> commentsCollection;

    @Autowired
    private MongoCommentNodeConverter mongoCommentNodeConverter;

    @Override
    public List<CommentNode> getComments(String postId) {
        // TODO: implement
        return List.of();
    }

    @Override
    public void addComment(String parentCommentId, String commentText) {
        // 1. First create a new comment entry using UUID
        // 2. Then find parent entry and update the children node by appending new UUID

        String newCommentId = UUID.randomUUID().toString();
        CommentNode newComment = CommentNode.builder()
                .commentId(newCommentId)
                .comment(commentText)
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .childrenNodes(List.of())
                .build();

        commentsCollection.insertOne(mongoCommentNodeConverter.convertCommentNodeToDocument(newComment));

        BasicDBObject findQuery = new BasicDBObject("commentId", parentCommentId);
        BasicDBObject pushQuery = new BasicDBObject("$push",
                new BasicDBObject("children", newComment));

        commentsCollection.updateOne(findQuery, pushQuery);
    }
}
