package apodviewer.comments.db;

import apodviewer.comments.model.CommentPointerNode;
import apodviewer.comments.model.CommentTreeNode;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MongoCommentNodeConverter {

    public Document convertCommentNodeToDocument(CommentPointerNode commentNode) {
        return new Document()
            .append("commentId", commentNode.getCommentId())
            .append("comment", commentNode.getComment())
            .append("parentId", commentNode.getParentId())
            .append("createDate", commentNode.getCreateDate())
            .append("modifiedDate", commentNode.getModifiedDate());
    }

    public CommentPointerNode convertDocumentToCommentPointerNode(Document document) {
        return CommentPointerNode.builder()
                .commentId(document.getString("commentId"))
                .parentId(document.getString("parentId"))
                .comment(document.getString("comment"))
                .createDate(convertToLocalDateTimeViaInstant(document.getDate("createDate")))
                .modifiedDate(convertToLocalDateTimeViaInstant(document.getDate("modifiedDate")))
                .build();
    }

    public CommentTreeNode convertDocumentToCommentTreeNode(Document document) {
        return CommentTreeNode.builder()
                .commentId(document.getString("commentId"))
                .children(List.of())
                .comment(document.getString("comment"))
                .createDate(convertToLocalDateTimeViaInstant(document.getDate("createDate")))
                .modifiedDate(convertToLocalDateTimeViaInstant(document.getDate("modifiedDate")))
                .build();
    }

    public CommentTreeNode convertPointerNodeToTreeNode(CommentPointerNode ptrNode) {
        return CommentTreeNode.builder()
                .commentId(ptrNode.getCommentId())
                .children(List.of())
                .comment(ptrNode.getComment())
                .createDate(ptrNode.getCreateDate())
                .modifiedDate(ptrNode.getModifiedDate())
                .build();
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}