package net.artux.pdanetwork.integration

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.user.CommandBlock
import net.artux.pdanetwork.utils.withBasicAuth
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.RequestEntity
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = ["default", "dev", "test"])
class QuestCommandsTest : AbstractTest() {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @org.junit.jupiter.api.Test
    fun `evaluate xp command`() {
        val response = restTemplate.exchange(
            RequestEntity
                .put("/api/v1/quest/commands")
                .withBasicAuth()
                .body(
                    CommandBlock().apply {
                        actions = LinkedHashMap()
                        actions["xp"] = listOf("5")
                    }
                ),
            String::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
    }
}
