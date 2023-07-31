package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;

import java.util.Set;
import java.util.UUID;

public interface ArticleService {

    ResponsePage<ArticleSimpleDto> getPageArticles(QueryPage page, Set<String> tags);

    ArticleDto getArticle(UUID id);

    ArticleSimpleDto createArticle(ArticleCreateDto createDto);

    ArticleSimpleDto editArticle(UUID id, ArticleCreateDto createDto);

    boolean deleteArticle(UUID id);

    boolean likeArticle(UUID id);

    Set<String> getTags();
}
