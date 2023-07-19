package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleFullDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.service.feed.ArticleService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Редактор ленты", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/feed")
@RequiredArgsConstructor
@ModeratorAccess
public class AdminArticlesController {

    private final ArticleService articleService;

    @GetMapping
    public ResponsePage<ArticleDto> getPageArticles(@Valid QueryPage page) {
        return articleService.getPageArticles(page);
    }

    @PostMapping("/create")
    @Operation(description = "Создание новой статьи")
    public ArticleDto addArticle(@Valid @RequestBody ArticleCreateDto createDto) {
        return articleService.addArticle(createDto);
    }

    @PostMapping("/edit/{id}")
    @Operation(description = "Редактирование статьи")
    public ArticleDto getArticle(@PathVariable UUID id, @Valid @RequestBody ArticleCreateDto createDto) {
        return articleService.editArticle(id, createDto);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение статьи в виде JSON")
    public ArticleFullDto getArticle(@PathVariable UUID id) {
        return articleService.getArticle(id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Удаление статьи")
    public boolean deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
        return true;
    }

}
