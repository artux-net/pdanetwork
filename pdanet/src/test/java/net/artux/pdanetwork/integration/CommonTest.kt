package net.artux.pdanetwork.integration

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.controller.rest.admin.quest.AdminQuestsController
import net.artux.pdanetwork.utils.withBasicAuth
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.RequestEntity
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = ["default", "dev", "test"])
class CommonTest : AbstractTest() {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var questController: AdminQuestsController

    @WithAnonymousUser
    @Test
    fun isEncWorks() {
        val response = restTemplate.getForEntity(
            "/enc",
            String::class.java
        )
        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
    }

    @WithAnonymousUser
    @Test
    fun isCommandsWorks() {
        val response = restTemplate.getForEntity<String>(
            "/api/v1/commands/server"
        )

        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
    }

    @WithAnonymousUser
    @Test
    fun isStatisticClosedForAnonymous() {
        val response = restTemplate.getForEntity<String>(
            "/api/v1/admin/statistic"
        )

        Assertions.assertEquals(401, response.statusCode.value())
    }

    @Test
    fun `get statistic with basic auth`() {
        val response = restTemplate.exchange(
            RequestEntity
                .get("/api/v1/admin/statistic")
                .withBasicAuth()
                .build(),
            String::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun maps() {
        val maps = questController.maps
        println(maps.contentToString())
    }
}
