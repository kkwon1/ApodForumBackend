package apodviewer.comments.db;

import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;
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

import static com.mongodb.client.model.Filters.eq;

@Component
public class MongoCommentsDao implements CommentsDao {

    @Autowired
    @Qualifier("commentsCollection")
    private MongoCollection<Document> commentsCollection;

    @Autowired
    private MongoCommentNodeConverter mongoCommentNodeConverter;

    @Override
    public CommentTree getPostComments(String postId) {
        BasicDBObject findQuery = new BasicDBObject("commentId", postId);

        MongoCursor<Document> cursor = commentsCollection.find(findQuery).cursor();
        Comment ptrRootNode = mongoCommentNodeConverter.convertDocumentToComment(cursor.next());

        CommentTree rootNode = mongoCommentNodeConverter.convertCommentToTree(ptrRootNode);
        populateCommentWithChildren(rootNode);
        return rootNode;
    }

    /**
     * We don't want to actually delete the record in the DB, because we still need to build the comment tree.
     * We should add a tombstone to the comment (can allow an "undo" for the delete? or should we keep it permanent?)
     *
     * When returning comments for post, hide the comment as "Deleted"
     * @param commentId
     * @return
     */
    @Override
    public String deleteComment(String commentId) {
        Bson query = eq("commentId", commentId);
        Bson updates = Updates.combine(
                Updates.set("isDeleted", true),
                Updates.set("modifiedDate", LocalDateTime.now()));
        UpdateOptions options = new UpdateOptions().upsert(true);

        commentsCollection.updateOne(query, updates, options);
        return commentId;
    }

    @Override
    public void addComment(Comment comment) {
        commentsCollection.insertOne(mongoCommentNodeConverter.convertCommentToDocument(comment));
    }

    private void populateCommentWithChildren(CommentTree root) {
        BasicDBObject findQuery = new BasicDBObject("parentId", root.getCommentId());
        MongoCursor<Document> cursor = commentsCollection.find(findQuery).cursor();

        List<CommentTree> children = new ArrayList<>();
        while (cursor.hasNext()) {
            children.add(mongoCommentNodeConverter.convertDocumentToCommentTree(cursor.next()));
        }

        root.setChildren(children);
        children.stream().filter(this::hasChildren)
                .forEach(this::populateCommentWithChildren);
    }

    private boolean hasChildren(CommentTree node) {
        return node.getIsLeaf() != null && !node.getIsLeaf();
    }
}
