package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleFullDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;

import java.util.UUID;

public interface FeedService {

    ArticleFullDto getArticle(UUID id);

    void addArticle(ArticleEntity article);

    ArticleDto addArticle(ArticleCreateDto createDto);

    void deleteArticle(UUID id);

    ResponsePage<ArticleDto> getPageArticles(QueryPage page);

    ArticleDto editArticle(UUID id, ArticleCreateDto createDto);

}
