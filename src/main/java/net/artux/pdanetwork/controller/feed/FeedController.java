package net.artux.pdanetwork.controller.feed;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.service.feed.FeedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@ApiIgnore
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    @ResponseBody
    @GetMapping
    public ResponsePage<ArticleEntity> getPageArticles(@Valid QueryPage page) {
        return feedService.getPageArticles(page);
    }

    @GetMapping("/{id}")
    public String getArticlePage(Model model, @PathVariable Long id) {
        ArticleEntity article = feedService.getArticle(id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("content", article.getContent());
        //model.addAttribute("tags", article.getTags());
        model.addAttribute("date", df.format(new Date(article.getPublished())));
        model.addAttribute("comments", 0);
        return "article";
    }

}