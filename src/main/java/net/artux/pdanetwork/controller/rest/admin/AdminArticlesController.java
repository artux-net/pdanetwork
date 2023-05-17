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
import net.artux.pdanetwork.service.feed.FeedService;
import net.artux.pdanetwork.utills.IsModerator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Редактор ленты", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/feed")
@RequiredArgsConstructor
@IsModerator
public class AdminArticlesController {

    private final FeedService feedService;

    @GetMapping
    public ResponsePage<ArticleDto> getPageArticles(@Valid QueryPage page) {
        return feedService.getPageArticles(page);
    }

    @PostMapping("/create")
    @Operation(description = "Создание новой статьи")
    public ArticleDto addArticle(@Valid ArticleCreateDto createDto) {
        return feedService.addArticle(createDto);
    }

    @PostMapping("/edit/{id}")
    @Operation(description = "Редактирование статьи")
    public ArticleDto getArticle(@PathVariable UUID id, @Valid ArticleCreateDto createDto) {
        return feedService.editArticle(id, createDto);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение статьи в виде JSON")
    public ArticleFullDto getArticle(@PathVariable UUID id) {
        return feedService.getArticle(id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Удаление статьи")
    public boolean deleteArticle(@PathVariable UUID id) {
        feedService.deleteArticle(id);
        return true;
    }

}
