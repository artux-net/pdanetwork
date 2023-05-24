package net.artux.pdanetwork;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationTest {

    @Autowired
    Validator validator;

    private ArticleCreateDto getTestDto(){
        ArticleCreateDto createDto = new ArticleCreateDto();
        createDto.setTitle("test title");
        createDto.setImage("https://klike.net/uploads/posts/2020-04/1587719791_1.jpg");
        createDto.setDescription("desc");
        createDto.setContent("123123");
        return createDto;
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
}