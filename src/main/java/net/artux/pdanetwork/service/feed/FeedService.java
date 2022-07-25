package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.models.chat.ArticleEntity;

public interface FeedService {

  ArticleEntity getArticle(Long id);
  void addArticle(ArticleEntity article);
  void deleteArticle(String id);
  ResponsePage<ArticleEntity> getPageArticles(QueryPage page);
  //Status editMember(RegisterUser user);

}
