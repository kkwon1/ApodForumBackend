package apodviewer.comments.db;

import apodviewer.comments.model.CommentNode;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MongoCommentNodeConverter {

    public Document convertCommentNodeToDocument(CommentNode commentNode) {
        return new Document()
            .append("commentId", commentNode.getCommentId())
            .append("comment", commentNode.getComment())
            .append("children", commentNode.getChildrenNodes())
            .append("createDate", commentNode.getCreateDate())
            .append("modifiedDate", commentNode.getModifiedDate());
    }

    public CommentNode convertDocumentToCommentNode(Document document) {
        return CommentNode.builder()
                .commentId(document.getString("commentId"))
                .childrenNodes(document.getList("children", CommentNode.class))
                .comment(document.getString("comment"))
                .createDate(convertToLocalDateTimeViaInstant(document.getDate("createDate")))
                .modifiedDate(convertToLocalDateTimeViaInstant(document.getDate("modifiedDate")))
                .build();
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
