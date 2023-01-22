package apodviewer.comments.db;

import apodviewer.comments.model.AddCommentRequest;
import apodviewer.comments.model.CommentPointerNode;
import apodviewer.comments.model.CommentTreeNode;
import apodviewer.comments.model.DeleteCommentRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Component
public class MongoCommentsClient implements CommentsClient {

    @Autowired
    @Qualifier("commentsCollection")
    private MongoCollection<Document> commentsCollection;

    @Autowired
    private MongoCommentNodeConverter mongoCommentNodeConverter;

    @Override
    public CommentTreeNode getAllComments(String postId) {
        BasicDBObject findQuery = new BasicDBObject("commentId", postId);

        MongoCursor<Document> cursor = commentsCollection.find(findQuery).cursor();
        CommentPointerNode ptrRootNode = mongoCommentNodeConverter.convertDocumentToCommentPointerNode(cursor.next());

        CommentTreeNode rootNode = mongoCommentNodeConverter.convertPointerNodeToTreeNode(ptrRootNode);
        getComments(rootNode);
        return rootNode;
    }


    /**
     * We don't want to actually delete the record in the DB, because we still need to build the comment tree.
     * We should add a tombstone to the comment (can allow an "undo" for the delete? or should we keep it permanent?)
     *
     * When returning comments for post, hide the comment as "Deleted"
     * @param deleteCommentRequest
     * @return
     */
    @Override
    public String deleteComment(DeleteCommentRequest deleteCommentRequest) {
        String commentId = deleteCommentRequest.getCommentId();
        Bson query = eq("commentId", commentId);
        Bson updates = Updates.combine(
                Updates.set("isDeleted", true),
                Updates.set("modifiedDate", LocalDateTime.now()));
        UpdateOptions options = new UpdateOptions().upsert(true);

        commentsCollection.updateOne(query, updates, options);
        return commentId;
    }

    @Override
    public String addComment(AddCommentRequest addCommentRequest) {
        String newCommentId = UUID.randomUUID().toString();
        CommentPointerNode newComment = CommentPointerNode.builder()
                .commentId(newCommentId)
                .comment(addCommentRequest.getComment())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .parentCommentId(addCommentRequest.getParentCommentId())
                .author(addCommentRequest.getAuthor())
                .build();

        commentsCollection.insertOne(mongoCommentNodeConverter.convertCommentNodeToDocument(newComment));

        return newComment.getCommentId();
    }

    private CommentTreeNode getComments(CommentTreeNode root) {
        BasicDBObject findQuery = new BasicDBObject("parentId", root.getCommentId());
        MongoCursor<Document> cursor = commentsCollection.find(findQuery).cursor();

        List<CommentTreeNode> children = new ArrayList<>();
        while (cursor.hasNext()) {
            children.add(mongoCommentNodeConverter.convertDocumentToCommentTreeNode(cursor.next()));
        }

        if (children.isEmpty()) {
            return root;
        } else {
            root.setChildren(children);
            children.forEach(this::getComments);
        }

        return root;
    }
}
