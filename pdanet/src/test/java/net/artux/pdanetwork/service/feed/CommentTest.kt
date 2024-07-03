package net.artux.pdanetwork.service.feed

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.models.feed.ArticleCreateDto
import net.artux.pdanetwork.models.feed.CommentCreateDto
import net.artux.pdanetwork.models.feed.CommentType
import org.junit.jupiter.api.Assertions
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
    private val articleService: ArticleService? = null

    @Autowired
    private val commentService: CommentService? = null
    private var articleId: UUID? = null
    private var commentId: UUID? = null

    @Test
    @Order(1)
    fun createArticle() {
        val dto = articleService!!.createArticle(testDto)
        articleId = dto.id
        Assertions.assertEquals(dto.description, testDto.description)
    }

    @Test
    @Order(2)
    fun commentArticle() {
        val createDto = CommentCreateDto()
        createDto.content = "test"
        val dto = commentService!!.comment(CommentType.ARTICLE, articleId, createDto)
        commentId = dto.id
        Assertions.assertEquals(1, commentService.getComments(CommentType.ARTICLE, articleId, QueryPage()).content.size)
    }

    @Test
    @Order(3)
    fun likeComment() {
        val result = commentService!!.likeComment(commentId)
        Assertions.assertTrue(result)
        Assertions.assertEquals(1, commentService.getComment(commentId).likes)
    }

    @Test
    @Order(4)
    fun dislikeComment() {
        val result = commentService!!.likeComment(commentId)
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
