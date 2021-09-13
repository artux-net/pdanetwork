package net.artux.pdanetwork.service.feed;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.repository.ArticleRepository;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.service.util.SortService;
import net.artux.pdanetwork.models.feed.Article;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

  private final ArticleRepository articleRepository;
  private final SortService sortService;
  private final PageService pageService;

  @Override
  public Article getArticle(String id) {
    return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find article"));
  }

  @Override
  public void addArticle(Article article) {
    Optional<Article> optionalArticle = articleRepository.findById(article.getId());
    if(optionalArticle.isPresent()){
      if (!optionalArticle.get().equals(article)){
        articleRepository.save(article);
      }
    }else {
      articleRepository.save(article);
    }
  }

  @Override
  public void deleteArticle(String id) {
    articleRepository.deleteById(new ObjectId(id));
  }

  @Override
  public ResponsePage<Article> getPageArticles(QueryPage queryPage) {
    Page<Article> page = articleRepository.findAll(sortService.getSortInfo(Article.class, queryPage, "published"));
    return pageService.mapDataPageToResponsePage(page, page.getContent());
  }

}
