package net.artux.pdanetwork.feed;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.feed.ArticleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Set;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
public class ArticleTest {

    @Autowired
    Validator validator;

    @Autowired
    private ArticleService articleService;
    private UUID testId;

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
        testId = dto.getId();
        Assertions.assertEquals(dto.getDescription(), getTestDto().getDescription());
    }

    @Test
    public void articleValidation() {
        Set<ConstraintViolation<ArticleCreateDto>> violations = validator.validate(getTestDto());
        System.out.println(violations.toString());
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void imageLinkFailed() {
        ArticleCreateDto createDto = getTestDto();
        createDto.setImage("123123");

        Set<ConstraintViolation<ArticleCreateDto>> violations = validator.validate(createDto);

        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyContentFailed() {
        ArticleCreateDto createDto = getTestDto();
        createDto.setContent("");

        Set<ConstraintViolation<ArticleCreateDto>> violations = validator.validate(createDto);

        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @Order(2)
    public void likeArticle() {
        boolean result = articleService.likeArticle(testId);
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, articleService.getArticle(testId).likes());
    }

    @Test
    @Order(3)
    public void dislikeArticle() {
        boolean result = articleService.likeArticle(testId);
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, articleService.getArticle(testId).likes());
    }

    @Test
    @Order(4)
    public void addTags() {
        ArticleCreateDto createDto = getTestDto();
        Set<String> tags = Set.of("tag1", "tag2");
        createDto.setTags(tags);
        articleService.editArticle(testId, createDto);

        Assertions.assertEquals(tags, articleService.getArticle(testId).tags());
    }

    @Test
    @Order(5)
    public void removeTag() {
        ArticleCreateDto createDto = getTestDto();
        Set<String> tags = Set.of("tag1", "tag3");
        createDto.setTags(tags);
        articleService.editArticle(testId, createDto);
        Assertions.assertEquals(tags, articleService.getArticle(testId).tags());
    }

    @Test
    @Order(6)
    public void getTags() {
        Assertions.assertEquals(Set.of("tag1", "tag2", "tag3"), articleService.getTags());
    }

    @Test
    @Order(7)
    public void checkGetPageByTags() {
        Assertions.assertEquals(1, articleService.getPageArticles(new QueryPage(), Set.of("tag1")).getContent().size());
    }

    @Test
    @Order(8)
    @Disabled
    public void checkEmptyGetPageByTags() {
        //TODO
        Assertions.assertEquals(1, articleService.getPageArticles(new QueryPage(), Set.of("tag3", "tag2")).getContent().size());
    }

}