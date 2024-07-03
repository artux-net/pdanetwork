package net.artux.pdanetwork.service.actions

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.controller.rest.admin.quest.AdminQuestsController
import net.artux.pdanetwork.models.quest.Chapter
import net.artux.pdanetwork.models.quest.Story
import net.artux.pdanetwork.models.quest.map.Point
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import java.util.*
import java.util.List
import java.util.Map

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestTest : AbstractTest() {

    @Autowired
    private val adminQuestsController: AdminQuestsController? = null

    @Test
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun info() {
        println(adminQuestsController!!.status)
    }

    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    @Test
    fun isStatisticClosed() {
        val story = Story()
        story.id = 1L
        story.title = "title"
        val chapter = Chapter()
        chapter.id = 1L
        chapter.points = Map.of(
            1L,
            listOf(Point())
        )
    }

    /**
     * this method is testing adding new story with two chapters of ten stages and two points in each chapter *
     */
    @Test
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun testStory() {
        val story = Story()
        story.id = 1L
        story.title = "title"
        val chapter1 = Chapter()
        chapter1.id = 1L
        chapter1.points = Map.of(
            1L,
            listOf(generatePoint())
        )
        val chapter2 = Chapter()
        chapter2.id = 2L
        chapter2.points = Map.of(
            2L,
            listOf(generatePoint())
        )
        val chapter3 = Chapter()
        chapter3.id = 3L
        chapter3.points =
            Map.of(
                3L,
                listOf(generatePoint())
            )
        val chapter4 = Chapter()
        chapter4.id = 4L
        chapter4.points =
            Map.of(
                4L,
                listOf(generatePoint())
            )
        val chapter5 = Chapter()
        chapter5.id = 5L
        chapter5.points =
            Map.of(
                5L,
                listOf()
            )
        story.chapters = List.of(chapter1, chapter2, chapter3, chapter4, chapter5)
    }

    /**
     * @return random point with random information
     */
    fun generatePoint(): Point {
        val point = Point()
        point.id = UUID.randomUUID()
        point.name = "Some point"
        return point
    }

    fun generateRandomPoint(): Point {
        val point = Point()
        point.id = UUID.randomUUID()
        point.name = "Some point"
        return point
    }
}
