package apodviewer.comments.util;

import apodviewer.comments.db.CommentsDao;
import apodviewer.comments.model.CommentTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class CommentHelperTest {

    @Mock
    private CommentsDao mockCommentsDao;

    private CommentHelper commentHelper;

    @BeforeEach
    public void setup(){
        commentHelper = new CommentHelper(mockCommentsDao);
    }

    @Test
    public void testGetCommentCountRootOnly() {
        CommentTree testTreeNode = CommentTree.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of())
                .build();

        when(mockCommentsDao.getPostComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(0, actualCount);
    }

    @Test
    public void testGetCommentCountChildren() {
        CommentTree testTreeNode = CommentTree.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of(getCommentTreeNode(List.of()), getCommentTreeNode(List.of()), getCommentTreeNode(List.of())))
                .build();

        when(mockCommentsDao.getPostComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(3, actualCount);
    }

    @Test
    public void testGetCommentCountNestedChildren() {
        List<CommentTree> testChildren = List.of(getCommentTreeNode(List.of()), getCommentTreeNode(List.of()), getCommentTreeNode(List.of()));
        CommentTree testTreeNode = CommentTree.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of(getCommentTreeNode(testChildren), getCommentTreeNode(testChildren), getCommentTreeNode(testChildren)))
                .build();

        when(mockCommentsDao.getPostComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(12, actualCount);
    }

    @Test
    public void testGetCommentCountDeepNestedChildren() {
        CommentTree testTreeNode = CommentTree.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of(CommentTree.builder()
                        .commentId("child")
                        .comment("text")
                        .children(List.of(CommentTree.builder()
                                .commentId("child")
                                .comment("text")
                                .children(List.of(CommentTree.builder()
                                        .commentId("child")
                                        .comment("text")
                                        .children(List.of(CommentTree.builder()
                                                .commentId("child")
                                                .comment("text")
                                                .children(List.of())
                                                .build()))
                                        .build()))
                                .build()))
                        .build()))
                .build();

        when(mockCommentsDao.getPostComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(4, actualCount);
    }

    private CommentTree getCommentTreeNode(List<CommentTree> children) {
        return CommentTree.builder()
                .commentId("someId")
                .comment("someComment")
                .children(children)
                .build();
    }
}
