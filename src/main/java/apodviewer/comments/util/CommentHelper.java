package apodviewer.comments.util;

import apodviewer.comments.db.CommentsClient;
import apodviewer.comments.model.CommentTreeNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
@Component
public class CommentHelper {

    @Autowired
    private final CommentsClient commentsClient;

    public int getCommentCount(String postId) {
        CommentTreeNode commentTreeNode = commentsClient.getAllComments(postId);
        List<CommentTreeNode> topLevelComments = commentTreeNode.getChildren();

        int totalNumberOfComments = 0;
        Queue<CommentTreeNode> queue = new LinkedList<>();
        if (!topLevelComments.isEmpty()) {
            queue.addAll(topLevelComments);
            totalNumberOfComments = queue.size();
        }

        while (!queue.isEmpty()) {
            CommentTreeNode node = queue.poll();
            List<CommentTreeNode> children = node.getChildren();
            totalNumberOfComments += children.size();
            queue.addAll(children);
        }

        return totalNumberOfComments;
    }
}
