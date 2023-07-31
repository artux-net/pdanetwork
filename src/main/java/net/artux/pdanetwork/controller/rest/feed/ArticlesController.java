package net.artux.pdanetwork.controller.rest.feed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.service.feed.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@Tag(name = "Новости")
@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Новости списком с пагинацией")
    public ResponsePage<ArticleSimpleDto> getPageArticles(@Valid QueryPage page, @RequestParam("tags") Set<String> tags) {
        return articleService.getPageArticles(page, tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение статьи в виде JSON")
    public ArticleDto getArticle(@PathVariable UUID id) {
        return articleService.getArticle(id);
    }

    @GetMapping("/{id}/like")
    @Operation(summary = "Лайк статьи")
    public boolean likeArticle(@PathVariable UUID id) {
        return articleService.likeArticle(id);
    }

    @GetMapping("/tags")
    public Set<String> getTags() {
        return articleService.getTags();
    }

}
