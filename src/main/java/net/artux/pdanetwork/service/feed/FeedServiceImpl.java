package net.artux.pdanetwork.service.feed;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleMapper;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.repository.feed.ArticleRepository;
import net.artux.pdanetwork.service.util.PageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final ArticleRepository articleRepository;
    private final PageService pageService;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleEntity getArticle(UUID id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find article"));
    }

    @Override
    public void addArticle(ArticleEntity article) {
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(UUID id) {
        articleRepository.deleteById(id);
    }

    @Override
    public ResponsePage<ArticleDto> getPageArticles(QueryPage queryPage) {
        Page<ArticleEntity> page = articleRepository.findAll(pageService.getPageable(queryPage));
        return pageService.mapDataPageToResponsePage(page, articleMapper.dto(page.getContent()));
    }

}
