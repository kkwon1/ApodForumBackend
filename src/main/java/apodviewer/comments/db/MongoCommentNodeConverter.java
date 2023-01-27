package apodviewer.comments.db;

import apodviewer.comments.model.Comment;
import apodviewer.comments.model.CommentTree;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MongoCommentNodeConverter {

    public Document convertCommentToDocument(Comment comment) {
        return new Document()
            .append("commentId", comment.getCommentId())
            .append("commentText", comment.getCommentText())
            .append("parentId", comment.getParentCommentId())
            .append("createDate", comment.getCreateDate())
            .append("modifiedDate", comment.getModifiedDate())
            .append("author", comment.getAuthor())
            .append("isDeleted", comment.getIsDeleted())
            .append("isLeaf", comment.getIsLeaf());
    }

    public Comment convertDocumentToComment(Document document) {
        return Comment.builder()
                .commentId(document.getString("commentId"))
                .parentCommentId(document.getString("parentId"))
                .commentText(document.getString("commentText"))
                .createDate(convertToLocalDateTimeViaInstant(document.getDate("createDate")))
                .modifiedDate(convertToLocalDateTimeViaInstant(document.getDate("modifiedDate")))
                .author(document.getString("author"))
                .isDeleted(document.getBoolean("isDeleted"))
                .isLeaf(document.getBoolean("isLeaf"))
                .build();
    }

    public CommentTree convertDocumentToCommentTree(Document document) {
        return CommentTree.builder()
                .commentId(document.getString("commentId"))
                .children(List.of())
                .comment(document.getString("commentText"))
                .createDate(convertToLocalDateTimeViaInstant(document.getDate("createDate")))
                .modifiedDate(convertToLocalDateTimeViaInstant(document.getDate("modifiedDate")))
                .author(document.getString("author"))
                .isDeleted(document.getBoolean("isDeleted"))
                .isLeaf(document.getBoolean("isLeaf"))
                .build();
    }

    public CommentTree convertCommentToTree(Comment comment) {
        return CommentTree.builder()
                .commentId(comment.getCommentId())
                .children(List.of())
                .comment(comment.getCommentText())
                .createDate(comment.getCreateDate())
                .modifiedDate(comment.getModifiedDate())
                .author(comment.getAuthor())
                .isDeleted(comment.getIsDeleted())
                .isLeaf(comment.getIsLeaf())
                .build();
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
