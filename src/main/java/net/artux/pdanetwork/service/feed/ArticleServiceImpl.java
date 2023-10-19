package net.artux.pdanetwork.service.feed;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.ArticleLikeEntity;
import net.artux.pdanetwork.entity.feed.LikeArticleId;
import net.artux.pdanetwork.entity.feed.TagEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.feed.ArticleCreateDto;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.feed.FeedMapper;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.repository.feed.ArticleLikeRepository;
import net.artux.pdanetwork.repository.feed.ArticleRepository;
import net.artux.pdanetwork.repository.feed.TagRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final UserService userService;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final TagRepository tagRepository;
    private final PageService pageService;
    private final FeedMapper feedMapper;
    private final EntityManager entityManager;

    @Override
    public ArticleDto getArticle(UUID id) {
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));

        article.setViews(article.getViews() + 1);
        articleRepository.save(article);

        return articleRepository.findArticleDtoById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));
    }

    @Override
    @ModeratorAccess
    public ArticleSimpleDto createArticle(ArticleCreateDto createDto) {
        ArticleEntity article = feedMapper.entity(createDto);
        article = articleRepository.saveAndFlush(article);
        logger.info("Статья \"{}\" ({}) создана пользователем {}",
                article.getTitle(), article.getId(), userService.getUserById().getLogin());
        return articleRepository.findSimpleArticleDtoById(article.getId());
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
        Page<ArticleSimpleDto> page;
        if(tags == null || tags.isEmpty())
            page = articleRepository.findAllSimple(pageService.getPageable(queryPage));
        else {
            page = articleRepository
                    .findAllByTagsIn(tags, pageService.getPageable(queryPage));
        }
        return pageService.mapDataPageToResponsePage(page, page.getContent());
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
        tags.addAll(feedMapper.tags(createDto.getTags()));
        article.setTags(tags);

        logger.info("Статья \"{}\" ({}) изменена модератором {}", article.getTitle(), article.getId(), userService.getUserById().getLogin());
        articleRepository.save(article);
        return articleRepository.findSimpleArticleDtoById(id);
    }

    @Override
    @Transactional
    public boolean likeArticle(UUID id) {
        UserEntity user = userService.getUserById();
        ArticleEntity article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find article"));
        LikeArticleId articleId = new LikeArticleId(user.getId(), id);

        if (articleLikeRepository.existsById(articleId)) {
            articleLikeRepository.deleteById(articleId);
            return false;
        } else {
            articleLikeRepository.save(new ArticleLikeEntity(user, article));
            return true;
        }
    }

    @Override
    public Collection<String> getTags() {
        return tagRepository.findAll().stream()
                .map(TagEntity::getTitle)
                .toList();
    }

    @Override
    public Collection<String> getTagsByArticleId(UUID articleId) {
        return tagRepository.findAllByArticleId(articleId);
    }

    @Override
    public ArticleSimpleDto getSimpleArticle(UUID testId) {
        return articleRepository.findSimpleArticleDtoById(testId);
    }

}
