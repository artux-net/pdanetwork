package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.feed.CommentCreateDto;
import net.artux.pdanetwork.models.feed.CommentDto;
import net.artux.pdanetwork.models.feed.CommentType;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.feed.ArticleService;
import net.artux.pdanetwork.service.feed.CommentService;
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
public class CommentTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    private UUID articleId;
    private UUID commentId;

    private static ArticleCreateDto getTestDto() {
        ArticleCreateDto createDto = new ArticleCreateDto();
        createDto.setTitle("test title");
        createDto.setImage("https://klike.net/uploads/posts/2020-04/1587719791_1.jpg");
        createDto.setDescription("desc");
        createDto.setContent("123123");
        return createDto;
    }

    @Test
    @Order(1)
    public void createArticle() {
        ArticleSimpleDto dto = articleService.createArticle(getTestDto());
        articleId = dto.id();
        Assertions.assertEquals(dto.description(), getTestDto().getDescription());
    }


    @Test
    @Order(2)
    public void commentArticle() {
        CommentCreateDto createDto = new CommentCreateDto();
        createDto.setContent("test");
        CommentDto dto = commentService.comment(CommentType.ARTICLE, articleId, createDto);
        commentId = dto.getId();
        Assertions.assertEquals(1, commentService.getComments(CommentType.ARTICLE, articleId, new QueryPage()).getContent().size());
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