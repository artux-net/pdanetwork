package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.feed.CommentCreateDto;
import net.artux.pdanetwork.models.feed.CommentDto;
import net.artux.pdanetwork.models.feed.CommentType;
import net.artux.pdanetwork.models.feed.PostCreateDto;
import net.artux.pdanetwork.models.feed.PostDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.feed.CommentService;
import net.artux.pdanetwork.service.feed.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
public class PostTest {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    private UUID postId;
    private UUID commentId;

    private static PostCreateDto getTestDto() {
        PostCreateDto createDto = new PostCreateDto();
        createDto.setTitle("test title");
        createDto.setContent("123123");
        return createDto;
    }

    @Test
    @Order(1)
    public void createPost() {
        PostDto dto = postService.createPost(getTestDto());
        postId = dto.getId();
        Assertions.assertEquals(dto.getContent(), getTestDto().getContent());
    }


    @Test
    @Order(2)
    public void commentPost() {
        CommentCreateDto createDto = new CommentCreateDto();
        createDto.setContent("test");
        CommentDto dto = commentService.comment(CommentType.POST, postId, createDto);
        commentId = dto.getId();
        Assertions.assertEquals(1, commentService.getComments(CommentType.POST, postId, new QueryPage()).getContent().size());
    }

    @Test
    @Order(3)
    public void likeComment() {
        boolean result = commentService.likeComment(commentId);
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, commentService.getComment(commentId).getLikes());
    }

    @Test
    @Order(4)
    public void dislikeComment() {
        boolean result = commentService.likeComment(commentId);
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, commentService.getComment(commentId).getLikes());
    }

}