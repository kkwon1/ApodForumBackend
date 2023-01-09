package apodviewer.comments.db;

import apodviewer.comments.model.CommentPointerNode;
import apodviewer.comments.model.CommentTreeNode;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public String addComment(String parentCommentId, String commentText, String author) {
        String newCommentId = UUID.randomUUID().toString();
        CommentPointerNode newComment = CommentPointerNode.builder()
                .commentId(newCommentId)
                .comment(commentText)
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .parentId(parentCommentId)
                .author(author)
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
