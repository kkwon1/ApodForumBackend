package apodviewer.comments.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Value
public class CommentNode {
    String commentId;
    List<CommentNode> childrenNodes;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    String comment;
}
