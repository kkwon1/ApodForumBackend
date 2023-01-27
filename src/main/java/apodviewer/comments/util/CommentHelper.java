package apodviewer.comments.util;

import apodviewer.comments.db.CommentsDao;
import apodviewer.comments.model.CommentTree;
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
    private final CommentsDao commentsDao;

    public int getCommentCount(String postId) {
        CommentTree commentTree = commentsDao.getPostComments(postId);
        List<CommentTree> topLevelComments = commentTree.getChildren();

        int totalNumberOfComments = 0;
        Queue<CommentTree> queue = new LinkedList<>();
        if (!topLevelComments.isEmpty()) {
            queue.addAll(topLevelComments);
            totalNumberOfComments = queue.size();
        }

        while (!queue.isEmpty()) {
            CommentTree node = queue.poll();
            List<CommentTree> children = node.getChildren();
            totalNumberOfComments += children.size();
            queue.addAll(children);
        }

        return totalNumberOfComments;
    }
}
