package net.artux.pdanetwork.service.user

import lombok.extern.slf4j.Slf4j
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserValidatorTest : AbstractTest() {

    @Autowired
    private lateinit var userValidator: UserValidator

    @Test
    fun checkUser() {
        val status = userValidator.checkUser(
            RegisterUserDto().apply {
                email = "test@test.ru"
                nickname = "Витька Белый"
                password = "12345678"
            }
        )

        assertTrue(status.isSuccess)
    }
}
