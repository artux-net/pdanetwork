package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.service.feed.FeedService;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@Api(tags = "Статьи")
@RequestMapping("/feed")
public class FeedController {

  private final FeedService feedService;

  @ResponseBody
  @GetMapping
  public ResponsePage<Article> getPageArticles(@Valid QueryPage page){
    return feedService.getPageArticles(page);
  }

  @GetMapping("/{id}")
  public String getArticlePage(Model model, @PathVariable String id){
    Article article = feedService.getArticle(id);
    model.addAttribute("title", article.getTitle());
    model.addAttribute("content", article.getContent());
    model.addAttribute("tags", article.getTags());
    model.addAttribute("date", new Date(article.getPublished()).toString());
    model.addAttribute("comments", 0);
    return "article";
  }

}