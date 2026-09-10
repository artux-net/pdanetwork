package net.artux.pdanetwork.e2e

import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.CommandBlock
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.models.user.dto.StoryData
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

/**
 * Живой (не мокнутый) e2e-прогон основного пользовательского флоу против уже
 * развёрнутого инстанса pdanetwork: регистрация -> логин -> чтение сюжета ->
 * прохождение двух стадий пролога -> повторное чтение прогресса. Тестовый
 * пользователь всегда удаляется в [deleteTestUser] (даже если один из шагов
 * выше упал), чтобы прогон не оставлял мусорные аккаунты в базе.
 *
 * Гоняется отдельным Gradle-таском `e2eTest` (не входит в `test`/`check`,
 * см. build.gradle.kts), раз в сутки на dev и на prod через
 * .github/workflows/e2e.yml. Целевой инстанс задаётся переменной окружения
 * E2E_BASE_URL, например https://app.artux.net/pdanetwork.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserFlowE2ETest {

    private val baseUrl = System.getenv("E2E_BASE_URL")
        ?: error("E2E_BASE_URL is not set - point it at the deployment to test, e.g. https://dev.artux.net/pdanetwork")

    private val restTemplate = RestTemplate()

    private val email = "e2e-test-${System.currentTimeMillis()}@example.com"
    private val password = "TestPass123!"
    private val nickname = "E2eTest" + (1..6).map { ('a'..'z').random() }.joinToString("")

    private fun authHeaders(): HttpHeaders = HttpHeaders().apply { setBasicAuth(email, password) }

    @Test
    @Order(1)
    fun `register a new user`() {
        val dto = RegisterUserDto()
        dto.email = email
        dto.password = password
        dto.nickname = nickname

        val response = restTemplate.postForEntity(
            "$baseUrl/api/v1/user/register",
            HttpEntity(dto),
            Status::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        val status = response.body!!
        assertTrue(status.isSuccess(), "registration failed: ${status.description}")
    }

    @Test
    @Order(2)
    fun `log in and fetch basic user info`() {
        val response = restTemplate.exchange(
            "$baseUrl/api/v1/user/info",
            HttpMethod.GET,
            HttpEntity<Void>(authHeaders()),
            String::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.contains(email), "user/info response did not contain the registered email")
    }

    @Test
    @Order(3)
    fun `fresh account has no story progress yet`() {
        val storyData = fetchQuestInfo()
        assertTrue(storyData.storyStates.isNullOrEmpty(), "a brand new user should not have any story progress")
    }

    @Test
    @Order(4)
    fun `public story list is reachable`() {
        val response = restTemplate.exchange(
            "$baseUrl/api/v1/quest/stories/public",
            HttpMethod.GET,
            HttpEntity<Void>(authHeaders()),
            String::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(
            response.body!!.contains("\"id\":1"),
            "expected the prologue story (id 1) to be in the public story list"
        )
    }

    @Test
    @Order(5)
    fun `prologue chapter content loads`() {
        val response = restTemplate.exchange(
            "$baseUrl/api/v1/quest/1/1",
            HttpMethod.GET,
            HttpEntity<Void>(authHeaders()),
            String::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    @Order(6)
    fun `advance through the first two stages of the prologue`() {
        var storyData = applyStateCommand("1:1:0")
        assertEquals(0, storyData.storyStates.first { it.storyId == 1 }.stageId)

        storyData = applyStateCommand("1:1:1")
        assertEquals(
            1,
            storyData.storyStates.first { it.storyId == 1 }.stageId,
            "stage did not advance to 1 after the second command"
        )
    }

    @Test
    @Order(7)
    fun `progress is persisted when re-reading quest info`() {
        val state = fetchQuestInfo().storyStates.first { it.storyId == 1 }
        assertEquals(1, state.stageId)
        assertTrue(state.isCurrent())
    }

    private fun fetchQuestInfo(): StoryData {
        val response = restTemplate.exchange(
            "$baseUrl/api/v1/user/quest/info",
            HttpMethod.GET,
            HttpEntity<Void>(authHeaders()),
            StoryData::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        return response.body!!
    }

    private fun applyStateCommand(state: String): StoryData {
        val block = CommandBlock()
        block.actions = linkedMapOf("state" to listOf(state))

        val response = restTemplate.exchange(
            "$baseUrl/api/v1/user/commands",
            HttpMethod.PUT,
            HttpEntity(block, authHeaders()),
            StoryData::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        return response.body!!
    }

    /** Всегда удаляет тестового пользователя, даже если один из шагов выше упал. */
    @AfterAll
    fun deleteTestUser() {
        try {
            restTemplate.exchange(
                "$baseUrl/api/v1/user/delete",
                HttpMethod.DELETE,
                HttpEntity<Void>(authHeaders()),
                Boolean::class.java
            )
        } catch (e: Exception) {
            println("Cleanup warning: failed to delete e2e test user $email: ${e.message}")
        }
    }
}
