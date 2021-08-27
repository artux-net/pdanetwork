package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.handlers.ChatHandler;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.service.feed.FeedService;
import net.artux.pdanetwork.service.files.SellersService;
import net.artux.pdanetwork.service.files.Types;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import net.artux.pdanetwork.utills.mongo.MongoAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/utility")
@Api(tags = "Админ панель")
@RequiredArgsConstructor
public class AdminController {

  private final MemberService memberService;
  private final MongoAdmin mongoAdmin;
  private final FeedService feedService;

  private final ChatHandler chatHandler;
  private final Types types;
  private final SellersService sellersService;

  private static Date readTime;


  @Value("${server.host}")
  private String host;
  @Value("${server.websocket.protocol}")
  private String protocol;
  @Value("${server.servlet.contextPath}")
  private String context;

  @GetMapping
  public String getMainPage(Model model){
    Member member = memberService.getMember();

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    model.addAttribute("username", member.getLogin());
    model.addAttribute("server_time", df.format(new Date()));
    model.addAttribute("username", member.getLogin());

    model.addAttribute("total_registrations", mongoAdmin.getSize());
    model.addAttribute("rating", mongoAdmin.getRating(0));
    model.addAttribute("online", mongoAdmin.getOnline());
    model.addAttribute("registrations", mongoAdmin.getRegistrations());

    return "admin";
  }

  @PostMapping("/reset")
  public String reset(Model model){
    readTime = new Date();
    types.reset();
    sellersService.reset();

    return getSettings(model);
  }

  @GetMapping("/settings")
  public String getSettings(Model model){
    model.addAttribute("readTime", readTime);
    return "settings";
  }


  @PostMapping("/articles")
  public String addArticle(Model model, @ModelAttribute Article article){
    feedService.addArticle(article);
    return getArticlePage(model);
  }

  @GetMapping("/articles")
  public String getArticlePage(Model model){
    List<Article> articleList = feedService.getPageArticles(new QueryPage()).getData();
    model.addAttribute("articles", articleList);
    return "articles";
  }

  @GetMapping("/articles/")
  public String articleCreation(Model model){
    model.addAttribute("article", new Article());
    return "articleCreation";
  }

  @GetMapping("/articles/{id}")
  public String getArticle(Model model, @PathVariable String id){
    model.addAttribute("article", feedService.getArticle(id));
    return "articleCreation";
  }

  @GetMapping("/articles/remove/{id}")
  public String deleteArticle(Model model, @PathVariable String id){
    feedService.deleteArticle(id);
    return getArticlePage(model);
  }

  @GetMapping("/chat")
  public String getChatPage(Model model){
    model.addAttribute("url", protocol+"://" + host + context + "/chat");
    return "chat";
  }

  @PostMapping("/chat/ban/{id}")
  public String banUser(Model model, @PathVariable("id") Integer pdaId, @QueryParam("comment") String reason){
    chatHandler.addToBanList(pdaId, reason);
    return getChatPage(model);
  }

  @PostMapping("/chat/delete/{time}")
  public String deleteMessage(Model model, @PathVariable("time") Long time){
    chatHandler.removeMessage(time);
    return getChatPage(model);
  }

  @GetMapping("/users")
  public String getUsersPage(Model model){
    return "users";
  }

}
