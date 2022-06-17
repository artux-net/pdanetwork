package net.artux.pdanetwork.service.feed;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.ResponsePage;
import net.artux.pdanetwork.models.feed.ArticleEntity;
import net.artux.pdanetwork.repository.ArticleRepository;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.service.util.SortService;
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
    public ArticleEntity getArticle(String id) {//TODO
        return articleRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Can not find article"));
    }

    @Override
    public void addArticle(ArticleEntity article) {
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(String id) {
        articleRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public ResponsePage<ArticleEntity> getPageArticles(QueryPage queryPage) {
        Page<ArticleEntity> page = articleRepository.findAll(sortService.getSortInfo(ArticleEntity.class, queryPage, "published"));
        return pageService.mapDataPageToResponsePage(page, page.getContent());
    }

}
