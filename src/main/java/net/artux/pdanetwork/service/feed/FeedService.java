package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {

  Article getArticle(String id);
  String addArticle(Article article);
  void deleteArticle(String id);
  ResponsePage<Article> getPageArticles(QueryPage page);
  //Status editMember(RegisterUser user);

}
