package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;

import java.util.UUID;

public interface FeedService {

  ArticleEntity getArticle(UUID id);
  void addArticle(ArticleEntity article);
  void deleteArticle(UUID id);
  ResponsePage<ArticleEntity> getPageArticles(QueryPage page);
  //Status editMember(RegisterUser user);

}
