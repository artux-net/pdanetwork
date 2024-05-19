package net.artux.pdanetwork.controller.web.feed;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.service.feed.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@Tag(name = "Статьи")
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final ArticleService articleService;
    private static final DateTimeFormatter df = DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .withZone(ZoneId.systemDefault());

    @GetMapping("/{id}")
    public String getArticlePage(Model model, @PathVariable UUID id) {
        ArticleDto article = articleService.getArticle(id);
        model.addAttribute("title", article.title());
        model.addAttribute("content", article.content());
        //model.addAttribute("tags", article.tags());
        model.addAttribute("date", df.format(article.published()));
        model.addAttribute("comments", article.comments());
        model.addAttribute("likes", article.likes());
        return "article";
    }

}