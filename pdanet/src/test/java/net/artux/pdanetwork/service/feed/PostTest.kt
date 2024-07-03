package net.artux.pdanetwork.service.feed

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.models.feed.CommentCreateDto
import net.artux.pdanetwork.models.feed.CommentType
import net.artux.pdanetwork.models.feed.PostCreateDto
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
class PostTest : AbstractTest() {
    @Autowired
    private lateinit var postService: PostService

    @Autowired
    private lateinit var commentService: CommentService
    private var postId: UUID? = null
    private var commentId: UUID? = null

    @Test
    @Order(1)
    fun createPost() {
        val dto = postService.createPost(testDto)
        postId = dto.id
        Assertions.assertEquals(dto.content, testDto.content)
    }

    @Test
    @Order(2)
    fun commentPost() {
        val createDto = CommentCreateDto()
        createDto.content = "test"
        val dto = commentService.comment(CommentType.POST, postId, createDto)
        commentId = dto.id
        Assertions.assertEquals(1, commentService.getComments(CommentType.POST, postId, QueryPage()).content.size)
    }

    @Test
    @Order(3)
    fun likeComment() {
        val result = commentService.likeComment(commentId)
        Assertions.assertTrue(result)
        Assertions.assertEquals(1, commentService.getComment(commentId).likes)
    }

    @Test
    @Order(4)
    fun dislikeComment() {
        val result = commentService.likeComment(commentId)
        Assertions.assertFalse(result)
        Assertions.assertEquals(0, commentService.getComment(commentId).likes)
    }

    companion object {
        private val testDto: PostCreateDto
            private get() {
                val createDto = PostCreateDto()
                createDto.title = "test title"
                createDto.content = "123123"
                return createDto
            }
    }
}
