package net.artux.pdanetwork.service.feed;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleFullDto;
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
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final PageService pageService;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleFullDto getArticle(UUID id) {
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));
        return articleMapper.fullDto(article);
    }

    @Override
    public void addArticle(ArticleEntity article) {
        articleRepository.save(article);
    }

    @Override
    public ArticleDto addArticle(ArticleCreateDto createDto) {
        return null;
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

    @Override
    public ArticleDto editArticle(UUID id, ArticleCreateDto createDto) {
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));
        article.setTitle(createDto.getTitle());
        article.setDescription(createDto.getDescription());
        article.setImage(createDto.getImage());
        article.setContent(createDto.getContent());

        return articleMapper.dto(articleRepository.save(article));
    }

}
