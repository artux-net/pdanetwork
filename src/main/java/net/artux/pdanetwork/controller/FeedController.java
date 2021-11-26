package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.feed.Article;
import net.artux.pdanetwork.service.feed.FeedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@Api(tags = "Статьи")
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    @ResponseBody
    @GetMapping
    public ResponsePage<Article> getPageArticles(@Valid QueryPage page) {
        return feedService.getPageArticles(page);
    }

    @GetMapping("/{id}")
    public String getArticlePage(Model model, @PathVariable String id) {
        Article article = feedService.getArticle(id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("content", article.getContent());
        model.addAttribute("tags", article.getTags());
        model.addAttribute("date", df.format(new Date(article.getPublished())));
        model.addAttribute("comments", 0);
        return "article";
    }

}