package net.artux.pdanetwork.service.feed

import jakarta.validation.Validator
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.models.feed.ArticleCreateDto
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import java.util.*
import java.util.stream.Stream

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
@ActiveProfiles(profiles = ["default", "dev", "test"])
class ArticleTest : AbstractTest() {

    @Autowired
    var validator: Validator? = null

    @Autowired
    private lateinit var articleService: ArticleService

    private var testId: UUID? = null

    @Test
    @Order(1)
    fun createArticle() {
        val createDto = testDto
        val createdDto = articleService.createArticle(createDto)
        testId = createdDto.id
        val dto = articleService.getArticle(testId)
        Assertions.assertEquals(dto.description, createDto.description)
        Assertions.assertEquals(dto.image, createDto.image)
        Assertions.assertEquals(dto.title, createDto.title)
        Assertions.assertEquals(dto.content, createDto.content)
    }

    @Order(2)
    @Test
    fun simpleArticle() {
        Assertions.assertDoesNotThrow { articleService.getSimpleArticle(testId) }
    }

    @Order(3)
    @Test
    fun getArticle() {
        Assertions.assertDoesNotThrow {
            val articleDto = articleService.getArticle(testId)
            println(articleDto.toString())
        }
    }

    @Test
    fun removeArticle() {
        val dto = articleService!!.createArticle(testDto)
        testId = dto.id
        articleService.deleteArticle(testId)

        Assertions.assertThrows(Throwable::class.java) { articleService.getArticle(testId) }
    }

    @Test
    fun articleValidation() {
        val violations = validator!!.validate(
            testDto
        )
        println(violations.toString())
        Assertions.assertTrue(violations.isEmpty())
    }

    @Test
    fun imageLinkFailed() {
        val createDto = testDto
        createDto.image = "123123"
        val violations = validator!!.validate(createDto)
        Assertions.assertFalse(violations.isEmpty())
    }

    @Test
    fun emptyContentFailed() {
        val createDto = testDto
        createDto.content = ""
        val violations = validator!!.validate(createDto)
        Assertions.assertFalse(violations.isEmpty())
    }

    @Test
    @Order(4)
    fun likeArticle() {
        val result = articleService!!.likeArticle(testId)
        Assertions.assertTrue(result)
        Assertions.assertEquals(1, articleService.getArticle(testId).likes)
    }

    @Test
    @Disabled
    @Order(5)
    fun dislikeArticle() {
        //TODO
        val before = articleService!!.getArticle(testId).likes
        val result = articleService.likeArticle(testId)
        Assertions.assertFalse(result)
        articleService.getArticle(testId).likes
        Assertions.assertEquals(before - 1, articleService.getArticle(testId).likes)
    }

    @Test
    @Disabled
    @Order(6)
    fun likeArticleAgain() {
        val result = articleService!!.likeArticle(testId)
        Assertions.assertTrue(result)
        Assertions.assertEquals(1, articleService.getArticle(testId).likes)
    }

    @Test
    @Order(6)
    fun addTags() {
        val createDto = testDto
        val tags = setOf("tag1", "tag2")
        createDto.tags = tags
        articleService!!.editArticle(testId, createDto)
        Assertions.assertLinesMatch(tags.stream().sorted(), articleService.getTagsByArticleId(testId).stream().sorted())
    }

    @Test
    @Order(7)
    fun removeTag() {
        val createDto = testDto
        val tags = setOf("tag1", "tag3")
        createDto.tags = tags
        articleService!!.editArticle(testId, createDto)
        Assertions.assertLinesMatch(tags.stream().sorted(), articleService.getTagsByArticleId(testId).stream().sorted())
    }

    @get:Order(8)
    @get:Test
    val tags: Unit
        get() {
            Assertions.assertLinesMatch(Stream.of("tag1", "tag2", "tag3"), articleService!!.getTags().stream())
        }

    @Test
    @Order(9)
    fun checkGetPageByTag() {
        Assertions.assertEquals(1, articleService!!.getPageArticles(QueryPage(), setOf("tag1")).content.size)
    }

    @Test
    @Order(10)
    fun checkGetPageByTags() {
        Assertions.assertEquals(1, articleService!!.getPageArticles(QueryPage(), setOf("tag1", "tag3")).content.size)
    }

    @Test
    @Order(11)
    fun checkEmptyPageByTags() {
        println(articleService!!.getTags())
        println("Current:" + articleService.getPageArticles(QueryPage(), setOf()).content[0])
        Assertions.assertEquals(0, articleService.getPageArticles(QueryPage(), setOf("tag3", "tag2")).content.size)
    }

    @Test
    @Order(12)
    fun cacheCheck() {
        val articles1 = articleService!!.getPageArticles(QueryPage(), setOf()).content
        val articles2 = articleService.getPageArticles(QueryPage(), setOf()).content
        Assertions.assertSame(articles1, articles2)
    }

    companion object {
        private val testDto: ArticleCreateDto
            private get() {
                val createDto = ArticleCreateDto()
                createDto.title = "test title"
                createDto.image = "https://klike.net/uploads/posts/2020-04/1587719791_1.jpg"
                createDto.description = "desc"
                createDto.content = "123123"
                return createDto
            }
    }
}