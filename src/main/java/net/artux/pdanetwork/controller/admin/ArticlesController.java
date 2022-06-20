package net.artux.pdanetwork.controller.admin;

import net.artux.pdanetwork.communication.handlers.ChatHandler;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.feed.ArticleEntity;
import net.artux.pdanetwork.service.feed.FeedService;
import net.artux.pdanetwork.service.files.ItemProvider;
import net.artux.pdanetwork.service.files.SellerManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

@Controller
@ApiIgnore
@RequestMapping("/utility/articles")
public class ArticlesController extends BaseUtilityController {

    private final FeedService feedService;
    private final ChatHandler chatHandler;
    private final ItemProvider itemProvider;
    private final SellerManager sellerManager;
    private static Date readTime = new Date();

    public ArticlesController(FeedService feedService, ChatHandler chatHandler, ItemProvider itemProvider, SellerManager sellerManager) {
        super("Лента");
        this.feedService = feedService;
        this.chatHandler = chatHandler;
        this.itemProvider = itemProvider;
        this.sellerManager = sellerManager;
    }

    @Override
    protected Object getHome(Model model) {
        List<ArticleEntity> articleList = feedService.getPageArticles(new QueryPage()).getData();
        model.addAttribute("articles", articleList);
        return pageWithContent("articles/list", model);
    }

    @PostMapping("/create")
    public Object addArticle(Model model, @ModelAttribute ArticleEntity article) {
        feedService.addArticle(article);
        return redirect(getPageUrl(), model, null);
    }

    @GetMapping("/create")
    public String articleCreation(Model model) {
        model.addAttribute("article", new ArticleEntity());
        return pageWithContent("articles/creation", model);
    }

    @GetMapping("/edit/{id}")
    public String getArticle(Model model, @PathVariable String id) {
        model.addAttribute("article", feedService.getArticle(id));
        return pageWithContent("articles/creation", model);
    }

    @GetMapping("/remove/{id}")
    public Object deleteArticle(Model model, @PathVariable String id) {
        feedService.deleteArticle(id);
        return redirect(getPageUrl(), model, null);
    }

}
