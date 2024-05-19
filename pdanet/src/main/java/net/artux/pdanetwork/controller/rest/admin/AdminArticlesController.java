package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;
import net.artux.pdanetwork.service.feed.ArticleService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@Tag(name = "Редактор ленты", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/feed")
@RequiredArgsConstructor
@ModeratorAccess
public class AdminArticlesController {

    private final ArticleService articleService;

    @GetMapping
    public ResponsePage<ArticleSimpleDto> getPageArticles(@Valid QueryPage page, @RequestParam(value = "tags", required = false) Set<String> tags) {
        return articleService.getPageArticles(page, tags);
    }

    @PostMapping("/create")
    @Operation(description = "Создание новой статьи")
    public ArticleSimpleDto addArticle(@Valid @RequestBody ArticleCreateDto createDto) {
        return articleService.createArticle(createDto);
    }

    @PostMapping("/edit/{id}")
    @Operation(description = "Редактирование статьи")
    public ArticleSimpleDto getArticle(@PathVariable UUID id, @Valid @RequestBody ArticleCreateDto createDto) {
        return articleService.editArticle(id, createDto);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение статьи в виде JSON")
    public ArticleDto getArticle(@PathVariable UUID id) {
        return articleService.getArticle(id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Удаление статьи")
    public boolean deleteArticle(@PathVariable UUID id) {
        return articleService.deleteArticle(id);
    }

}
