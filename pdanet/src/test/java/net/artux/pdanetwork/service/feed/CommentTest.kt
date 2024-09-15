package net.artux.pdanetwork.service.feed

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.models.feed.ArticleCreateDto
import net.artux.pdanetwork.models.feed.CommentCreateDto
import net.artux.pdanetwork.models.feed.CommentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import java.util.UUID

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithUserDetails(value = "admin@artux.net")
class CommentTest : AbstractTest() {
    @Autowired
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var commentService: CommentService

    private var articleId: UUID? = null
    private var commentId: UUID? = null

    @Test
    @Order(1)
    fun createArticle() {
        val dto = articleService.createArticle(testDto)
        articleId = dto.id
        Assertions.assertEquals(dto.description, testDto.description)
    }

    @Test
    @Order(2)
    fun commentArticle() {
        val createDto = CommentCreateDto()
        createDto.content = "test"
        val dto = commentService.comment(CommentType.ARTICLE, articleId, createDto)
        commentId = dto.id
        Assertions.assertEquals(1, commentService.getComments(CommentType.ARTICLE, articleId, QueryPage()).content.size)
    }

    @Test
    @Order(3)
    fun likeComment() {
        val result = commentService.likeComment(commentId)
        Assertions.assertTrue(result)
        Assertions.assertEquals(1, commentService.getComment(commentId).likes)
    }

    @Test
    @Order(3)
    @Disabled
    @Suppress("UnusedPrivateProperty")
    fun likeAllComment() {
        val articleId = articleService.createArticle(testDto).id

        for (i in 1..100) {
            val createDto = CommentCreateDto()
            createDto.content = "test"
            commentService.comment(CommentType.ARTICLE, articleId, createDto).id
        }
        val createDto = CommentCreateDto()
        createDto.content = "final test"
        val commentId = commentService.comment(CommentType.ARTICLE, articleId, createDto).id

        var likeFlag = true
        for (i in 1..100) {
            val result = commentService.likeComment(commentId)
            assertThat(result).isEqualTo(likeFlag)
            Assertions.assertEquals(if (likeFlag) 1 else 0, commentService.getComment(commentId).likes)
            likeFlag = !likeFlag
        }
    }

    @Test
    @Order(4)
    fun dislikeComment() {
        val result = commentService.likeComment(commentId)
        Assertions.assertFalse(result)
        Assertions.assertEquals(0, commentService.getComment(commentId).likes)
    }

    companion object {
        private val testDto: ArticleCreateDto
            get() {
                val createDto = ArticleCreateDto()
                createDto.title = "test title"
                createDto.image = "https://klike.net/uploads/posts/2020-04/1587719791_1.jpg"
                createDto.description = "desc"
                createDto.content = "123123"
                return createDto
            }
    }
}
