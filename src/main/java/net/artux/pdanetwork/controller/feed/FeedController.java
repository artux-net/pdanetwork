package net.artux.pdanetwork.controller.feed;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.service.feed.FeedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.UUID;

@Controller
@Tag(name = "Статьи")
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private static final DateTimeFormatter df = DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .withZone(ZoneId.systemDefault());

    @ResponseBody
    @GetMapping
    public ResponsePage<ArticleDto> getPageArticles(@Valid QueryPage page) {
        return feedService.getPageArticles(page);
    }

    @GetMapping("/{id}")
    public String getArticlePage(Model model, @PathVariable UUID id) {
        ArticleEntity article = feedService.getArticle(id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("content", article.getContent());
        //model.addAttribute("tags", article.getTags());
        model.addAttribute("date", df.format(article.getPublished()));
        model.addAttribute("comments", 0);
        return "article";
    }

}