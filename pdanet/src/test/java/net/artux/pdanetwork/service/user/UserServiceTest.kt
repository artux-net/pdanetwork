package net.artux.pdanetwork.service.user

import lombok.extern.slf4j.Slf4j
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.communication.ConversationCreateDTO
import net.artux.pdanetwork.models.communication.ConversationType
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.repository.user.UserConfirmationRepository
import net.artux.pdanetwork.service.action.ActionService
import net.artux.pdanetwork.service.communication.ConversationService
import net.artux.pdanetwork.service.email.EmailService
import net.artux.pdanetwork.service.user.reset.ResetService
import net.artux.pdanetwork.utills.RandomString
import net.artux.pdanetwork.utils.SecurityService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithUserDetails

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithUserDetails(value = "admin@artux.net")
@MockBean(
    classes = [
        EmailService::class
    ]
)
class UserServiceTest : AbstractTest() {

    @Autowired
    private lateinit var securityService: SecurityService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var actionService: ActionService

    @Autowired
    private lateinit var resetService: ResetService

    @Autowired
    private lateinit var conversationService: ConversationService

    @Autowired
    private lateinit var userConfirmationRepository: UserConfirmationRepository

    private val randomString = RandomString()
    private val registerUser: RegisterUserDto
        get() = RegisterUserDto.builder()
            .email(USER_TEST_EMAIL)
            .avatar("0")
            .nickname("test")
            .password("12345678")
            .build()

    @Test
    @Order(1)
    @WithAnonymousUser
    fun registerUser() {
        val registerStatus = userService.registerUser(registerUser)
        Assertions.assertTrue(registerStatus.isSuccess)

        val userConfirmationEntity = userConfirmationRepository.findAll().first()
        val confirmationStatus = userService.handleConfirmation(userConfirmationEntity.token)

        Assertions.assertTrue(confirmationStatus.isSuccess)
        Assertions.assertNotNull(userService.getUserByEmail(USER_TEST_EMAIL))
    }

    @Test
    @Order(2)
    @WithAnonymousUser
    fun login() {
        Assertions.assertTrue(securityService.isPasswordCorrect(USER_TEST_EMAIL, "12345678"))
    }

    @Test
    @Order(3)
    @WithAnonymousUser
    fun changePassword() {
        resetService.sendResetPasswordLetter(registerUser.email)
        val pass = randomString.nextString()
        val token = resetService.getTokens().stream().findAny().get()
        resetService.changePassword(token, pass)
        Assertions.assertTrue(securityService.isPasswordCorrect(registerUser.email, pass))
    }

    @Test
    fun checkIfAdminCreated() {
        val data = actionService.applyCommands(emptyMap())

        Assertions.assertEquals(ADMIN_TEST_EMAIL, data.login)
    }

    @Test
    fun `delete account`() {
        conversationService.createConversation(
            ConversationCreateDTO("test", "test", listOf(userService.getCurrentId()), ConversationType.PRIVATE)
        )
        actionService.applyCommands(mapOf("add" to listOf("31:1")))
        resetService.deleteAccount()
    }

    companion object {
        const val USER_TEST_EMAIL = "user@artux.net"
        const val ADMIN_TEST_EMAIL = "admin@artux.net"
    }
}
