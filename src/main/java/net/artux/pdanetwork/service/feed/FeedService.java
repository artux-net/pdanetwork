package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.feed.Article;

public interface FeedService {

  Article getArticle(String id);
  void addArticle(Article article);
  void deleteArticle(String id);
  ResponsePage<Article> getPageArticles(QueryPage page);
  //Status editMember(RegisterUser user);

}
