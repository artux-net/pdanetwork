package net.artux.pdanetwork.integration

import net.artux.pdanetwork.controller.rest.admin.quest.AdminQuestsController
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommonTest : AbstractTest() {

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Autowired
    private val questController: AdminQuestsController? = null

    @WithAnonymousUser
    @org.junit.Test
    fun isEncWorks() {
        val response = restTemplate!!.getForEntity(
            "/enc",
            String::class.java
        )
        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
    }

    @WithAnonymousUser
    @Test
    fun isCommandsWorks() {
        val response = restTemplate!!.getForEntity(
            "/api/v1/commands/server",
            String::class.java
        )
        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
    }

    @WithAnonymousUser
    @Test
    fun isStatisticClosedForAnonymous() {
        val response = restTemplate!!.getForEntity(
            "/api/v1/admin/statistic",
            String::class.java
        )
        Assertions.assertEquals(401, response.statusCode.value())
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun maps() {
        val maps = questController!!.maps
        println(maps.contentToString())
    }


}