package apodviewer.comments.util;

import apodviewer.comments.db.CommentsClient;
import apodviewer.comments.model.CommentTreeNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentHelperTest {

    @Mock
    private CommentsClient mockCommentsClient;

    private CommentHelper commentHelper;

    @Before
    public void setup(){
        commentHelper = new CommentHelper(mockCommentsClient);
    }

    @Test
    public void testGetCommentCountRootOnly() {
        CommentTreeNode testTreeNode = CommentTreeNode.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of())
                .build();

        when(mockCommentsClient.getAllComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(0, actualCount);
    }

    @Test
    public void testGetCommentCountChildren() {
        CommentTreeNode testTreeNode = CommentTreeNode.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of(getCommentTreeNode(List.of()), getCommentTreeNode(List.of()), getCommentTreeNode(List.of())))
                .build();

        when(mockCommentsClient.getAllComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(3, actualCount);
    }

    @Test
    public void testGetCommentCountNestedChildren() {
        List<CommentTreeNode> testChildren = List.of(getCommentTreeNode(List.of()), getCommentTreeNode(List.of()), getCommentTreeNode(List.of()));
        CommentTreeNode testTreeNode = CommentTreeNode.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of(getCommentTreeNode(testChildren), getCommentTreeNode(testChildren), getCommentTreeNode(testChildren)))
                .build();

        when(mockCommentsClient.getAllComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(12, actualCount);
    }

    @Test
    public void testGetCommentCountDeepNestedChildren() {
        CommentTreeNode testTreeNode = CommentTreeNode.builder()
                .commentId("testPost")
                .comment("rootComment")
                .children(List.of(CommentTreeNode.builder()
                        .commentId("child")
                        .comment("text")
                        .children(List.of(CommentTreeNode.builder()
                                .commentId("child")
                                .comment("text")
                                .children(List.of(CommentTreeNode.builder()
                                        .commentId("child")
                                        .comment("text")
                                        .children(List.of(CommentTreeNode.builder()
                                                .commentId("child")
                                                .comment("text")
                                                .children(List.of())
                                                .build()))
                                        .build()))
                                .build()))
                        .build()))
                .build();

        when(mockCommentsClient.getAllComments("testPost")).thenReturn(testTreeNode);

        int actualCount = commentHelper.getCommentCount("testPost");

        assertEquals(4, actualCount);
    }

    private CommentTreeNode getCommentTreeNode(List<CommentTreeNode> children) {
        return CommentTreeNode.builder()
                .commentId("someId")
                .comment("someComment")
                .children(children)
                .build();
    }
}
