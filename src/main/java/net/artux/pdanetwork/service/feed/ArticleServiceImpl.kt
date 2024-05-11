package net.artux.pdanetwork.service.feed

import jakarta.persistence.EntityNotFoundException
import lombok.RequiredArgsConstructor
import net.artux.pdanetwork.entity.feed.ArticleLikeEntity
import net.artux.pdanetwork.entity.feed.LikeArticleId
import net.artux.pdanetwork.entity.feed.TagEntity
import net.artux.pdanetwork.models.feed.ArticleCreateDto
import net.artux.pdanetwork.models.feed.ArticleDto
import net.artux.pdanetwork.models.feed.ArticleSimpleDto
import net.artux.pdanetwork.models.feed.FeedMapper
import net.artux.pdanetwork.models.page.QueryPage
import net.artux.pdanetwork.models.page.ResponsePage
import net.artux.pdanetwork.repository.feed.ArticleLikeRepository
import net.artux.pdanetwork.repository.feed.ArticleRepository
import net.artux.pdanetwork.repository.feed.TagRepository
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.service.util.PageService
import net.artux.pdanetwork.utills.security.ModeratorAccess
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@RequiredArgsConstructor
open class ArticleServiceImpl(
    private val userService: UserService,
    private val articleRepository: ArticleRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val tagRepository: TagRepository,
    private val pageService: PageService,
    private val feedMapper: FeedMapper,
) : ArticleService {
    private val logger: Logger = LoggerFactory.getLogger(ArticleServiceImpl::class.java)

    override fun getArticle(id: UUID): ArticleDto {
        val article = articleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Can not find article") }
        article.views += 1
        articleRepository.save(article)

        return articleRepository.findArticleDtoById(id)
            .orElseThrow { EntityNotFoundException("Can not find article") }
    }

    @ModeratorAccess
    @CacheEvict(CACHE_KEY)
    override fun createArticle(createDto: ArticleCreateDto): ArticleSimpleDto {
        var article = feedMapper.entity(createDto)
        article = articleRepository.saveAndFlush(article)
        logger.info("Статья \"${article.title}\" (${article.id}) создана пользователем ${userService.userById.login}")
        return articleRepository.findSimpleArticleDtoById(article.id)
    }

    @ModeratorAccess
    override fun deleteArticle(id: UUID): Boolean {
        logger.info("Статья \"${getArticle(id).title}\" (${id}) удалена пользователем ${userService.userById.login}")
        articleRepository.deleteById(id)
        return true
    }

    @Cacheable(CACHE_KEY)
    override fun getPageArticles(queryPage: QueryPage, tags: Set<String>): ResponsePage<ArticleSimpleDto> {
        val page = if (tags.isEmpty()) {
            articleRepository.findAllSimple(pageService.getPageable(queryPage))
        } else {
            articleRepository.findAllByTagsIn(tags, pageService.getPageable(queryPage))
        }
        return pageService.mapDataPageToResponsePage(page, page.content)
    }

    @ModeratorAccess
    override fun editArticle(id: UUID, createDto: ArticleCreateDto): ArticleSimpleDto {
        val article = articleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Can not find article") }

        article.title = createDto.title
        article.image = createDto.image
        article.content = createDto.content
        article.description = createDto.description
        val tags = tagRepository.findAllByTitleIn(createDto.tags)
        tags.addAll(feedMapper.tags(createDto.tags))
        article.tags = tags

        logger.info("Статья \"${article.title}\" (${article.id}) изменена модератором ${userService.userById.login}")
        articleRepository.save(article)
        return articleRepository.findSimpleArticleDtoById(id)
    }

    @Transactional
    override fun likeArticle(id: UUID): Boolean {
        val user = userService.userById
        val article = articleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Can not find article") }
        val articleId = LikeArticleId(user.id, id)

        if (articleLikeRepository.existsById(articleId)) {
            articleLikeRepository.deleteById(articleId)
            return false
        } else {
            articleLikeRepository.save(ArticleLikeEntity(user, article))
            return true
        }
    }

    override fun getTags(): Collection<String> {
        return tagRepository.findAll().stream()
            .map { obj: TagEntity -> obj.title }
            .toList()
    }

    override fun getTagsByArticleId(articleId: UUID): Collection<String> {
        return tagRepository.findAllByArticleId(articleId)
    }

    override fun getSimpleArticle(testId: UUID): ArticleSimpleDto {
        return articleRepository.findSimpleArticleDtoById(testId)
    }

    @Scheduled(cron = "0 * * * * *")
    @CacheEvict(CACHE_KEY)
    open fun evictCache() {
    }

    companion object {
        private const val CACHE_KEY = "FEED_CACHE"
    }
}
