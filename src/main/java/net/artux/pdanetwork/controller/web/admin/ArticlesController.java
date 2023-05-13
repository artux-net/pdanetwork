package net.artux.pdanetwork.controller.web.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.service.feed.FeedService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@Tag(name = "Лента")
@RequestMapping("/utility/articles")
public class ArticlesController extends BaseUtilityController {

    private final FeedService feedService;

    public ArticlesController(FeedService feedService) {
        super("Лента");
        this.feedService = feedService;
    }

    @Override
    protected Object getHome(Model model) {
        QueryPage queryPage = new QueryPage();
        queryPage.setSortBy("published");
        queryPage.setSortDirection(Sort.Direction.DESC);
        List<ArticleDto> articleList = feedService.getPageArticles(queryPage).getData();
        model.addAttribute("articles", articleList);
        return pageWithContent("articles/list", model);
    }

    @PostMapping("/save")
    public Object addArticle(Model model, @Valid @ModelAttribute ArticleEntity article) {
        feedService.addArticle(article);
        return redirect(getPageUrl(), model, null);
    }

    @GetMapping("/create")
    public String articleCreation(Model model) {
        model.addAttribute("article", new ArticleEntity());
        return pageWithContent("articles/edit", model);
    }

    @GetMapping("/edit/{id}")
    public String getArticle(Model model, @PathVariable UUID id) {
        model.addAttribute("article", feedService.getArticle(id));
        return pageWithContent("articles/edit", model);
    }

    @GetMapping("/remove/{id}")
    public Object deleteArticle(Model model, @PathVariable UUID id) {
        feedService.deleteArticle(id);
        return redirect(getPageUrl(), model, null);
    }

}
