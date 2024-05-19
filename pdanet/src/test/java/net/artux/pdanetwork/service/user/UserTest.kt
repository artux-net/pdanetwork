package net.artux.pdanetwork.service.user

import lombok.extern.slf4j.Slf4j
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.service.action.ActionService
import net.artux.pdanetwork.service.user.reset.ResetService
import net.artux.pdanetwork.service.util.SecurityService
import net.artux.pdanetwork.utills.RandomString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithUserDetails

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest : AbstractTest() {
    @Autowired
    private val securityService: SecurityService? = null

    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val actionService: ActionService? = null

    @Autowired
    private val resetService: ResetService? = null
    private val randomString = RandomString()
    val registerUser: RegisterUserDto
        get() = RegisterUserDto.builder()
            .login("test")
            .email("prygunovmaksim@yandex.ru")
            .avatar("0")
            .name("test")
            .nickname("test")
            .password("12345678")
            .build()

    @Test
    @WithAnonymousUser
    fun registerUser() {
        val status = userService!!.registerUser(registerUser)
        Assertions.assertTrue(status.isSuccess)
    }

    @Test
    @WithAnonymousUser
    fun login() {
        Assertions.assertTrue(securityService!!.isPasswordCorrect("test", "12345678"))
    }

    @Test
    @WithAnonymousUser
    fun changePassword() {
        resetService!!.sendResetPasswordLetter(registerUser.email)
        val pass = randomString.nextString()
        val token = resetService.getTokens().stream().findAny().get()
        resetService.changePassword(token, pass)
        Assertions.assertTrue(securityService!!.isPasswordCorrect(registerUser.login, pass))
    }

    @Test
    fun checkIfUserRegistered() {
        Assertions.assertNotNull(userService!!.getUserByLogin("test"))
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    fun checkIfAdminCreated() {
        val data = actionService!!.applyCommands(emptyMap())
        Assertions.assertEquals("admin", data.login)
    }
}