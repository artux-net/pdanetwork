package net.artux.pdanetwork.controller.rest.articles;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleFullDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.service.feed.FeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Лента новостей")
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class ArticlesController {

    private final FeedService feedService;

    @GetMapping
    public ResponsePage<ArticleDto> getPageArticles(@Valid QueryPage page) {
        return feedService.getPageArticles(page);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение статьи в виде JSON")
    public ArticleFullDto getArticle(@PathVariable UUID id) {
        return feedService.getArticle(id);
    }


}
