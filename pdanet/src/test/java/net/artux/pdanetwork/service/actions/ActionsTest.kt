package net.artux.pdanetwork.service.actions

import lombok.extern.slf4j.Slf4j
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.user.gang.Gang
import net.artux.pdanetwork.service.action.ActionService
import net.artux.pdanetwork.service.user.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@Slf4j
@ActiveProfiles(profiles = ["default", "dev", "test"])
@WithUserDetails(value = "admin@artux.net")
class ActionsTest : AbstractTest() {

    @Autowired
    private lateinit var actionService: ActionService

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun addMoney() {
        var user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        val money = user.money
        actionService.applyCommands(user.id, mapOf("money" to listOf("1000")))
        user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        Assertions.assertEquals(money + 1000, user.money as Int)
    }

    @AfterEach
    fun resetUser() {
        val user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        user.reset()
    }

    @Test
    @Order(1)
    fun addRelation() {
        var user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        actionService.applyCommands(user.id, mapOf("add" to listOf("relation_1:50")))
        user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        for (gang in Gang.entries) {
            if (gang == Gang.BANDITS) continue
            Assertions.assertEquals(0, user.gangRelation.getRelation(gang))
        }
        Assertions.assertEquals(50, user.gangRelation.bandits)
    }

    @Test
    @Order(2)
    fun removeRelation() {
        var user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        actionService.applyCommands(user.id, mapOf("add" to listOf("relation_1:-50")))
        user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        for (gang in Gang.entries) {
            if (gang == Gang.BANDITS) continue
            Assertions.assertEquals(0, user.gangRelation.getRelation(gang))
        }
        Assertions.assertEquals(0, user.gangRelation.getRelation(Gang.BANDITS))
    }

    @Test
    @Order(3)
    fun setRelation() {
        var user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        actionService.applyCommands(user.id, mapOf("relation" to listOf("MERCENARIES", "100")))
        user = userService.getUserByEmail(ADMIN_TEST_EMAIL)
        Assertions.assertEquals(100, user.gangRelation.mercenaries)
    }

    companion object {
        const val ADMIN_TEST_EMAIL = "admin@artux.net"
    }
}
