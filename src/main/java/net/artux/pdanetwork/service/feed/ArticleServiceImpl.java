package net.artux.pdanetwork.service.feed;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.TagEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.feed.FeedMapper;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.repository.feed.ArticleRepository;
import net.artux.pdanetwork.repository.feed.TagRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final UserService userService;
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final PageService pageService;
    private final FeedMapper feedMapper;

    @Override
    public ArticleDto getArticle(UUID id) {
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));
        return feedMapper.fullDto(article);
    }

    @Override
    @ModeratorAccess
    public ArticleSimpleDto createArticle(ArticleCreateDto createDto) {
        ArticleEntity article = feedMapper.entity(createDto);
        article = articleRepository.save(article);
        logger.info("Статья \"{}\" ({}) создана пользователем {}", article.getTitle(), article.getId(), userService.getUserById().getLogin());
        return feedMapper.dto(article);
    }

    @Override
    @ModeratorAccess
    public boolean deleteArticle(UUID id) {
        logger.info("Статья \"{}\" ({}) удалена пользователем {}", getArticle(id).title(), id, userService.getUserById().getLogin());
        articleRepository.deleteById(id);
        return true;
    }

    @Override
    public ResponsePage<ArticleSimpleDto> getPageArticles(QueryPage queryPage, Set<String> tags) {
        Page<ArticleEntity> page;
        if(tags == null || tags.isEmpty())
            page = articleRepository.findAll(pageService.getPageable(queryPage));
       else
            page = articleRepository
                    .findAllByTagsIn(tags, pageService.getPageable(queryPage));

        return pageService.mapDataPageToResponsePage(page, feedMapper.dto(page.getContent()));
    }

    @Override
    @ModeratorAccess
    public ArticleSimpleDto editArticle(UUID id, ArticleCreateDto createDto) {
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));

        article.setTitle(createDto.getTitle());
        article.setImage(createDto.getImage());
        article.setContent(createDto.getContent());
        article.setDescription(createDto.getDescription());
        Set<TagEntity> tags = tagRepository.findAllByTitleIn(createDto.getTags());
        tags.addAll(feedMapper.tag(createDto.getTags()));
        article.setTags(tags);

        logger.info("Статья \"{}\" ({}) изменена модератором {}", article.getTitle(), article.getId(), userService.getUserById().getLogin());

        return feedMapper.dto(articleRepository.save(article));
    }

    @Override
    public boolean likeArticle(UUID id) {
        UserEntity user = userService.getUserById();
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));
        boolean result = article.like(user);
        articleRepository.save(article);
        return result;
    }

    @Override
    public Set<String> getTags() {
        return tagRepository.findAll().stream().map(TagEntity::getTitle).collect(Collectors.toSet());
    }

}
