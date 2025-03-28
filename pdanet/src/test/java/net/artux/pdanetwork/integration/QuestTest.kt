package net.artux.pdanetwork.integration

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.utils.withBasicAuth
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.RequestEntity
import org.springframework.test.context.ActiveProfiles
import java.util.Locale

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = ["default", "dev", "test"])
class QuestTest : AbstractTest() {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @org.junit.jupiter.api.Test
    fun `get story with locale`() {
        val response = restTemplate.exchange(
            RequestEntity
                .get("/api/v1/quest/stories/public")
                .withBasicAuth()
                .headers {
                    it.acceptLanguageAsLocales = listOf(Locale.of("en"))
                }.build(),
            String::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
    }
}
