package net.artux.pdanetwork.service.actions

import lombok.extern.slf4j.Slf4j
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.user.gang.Gang
import net.artux.pdanetwork.service.action.ActionService
import net.artux.pdanetwork.service.user.UserService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import java.util.Map

@SpringBootTest
@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@Slf4j
@ActiveProfiles(profiles = ["default", "dev", "test"])
class ActionsTest : AbstractTest() {

    @Autowired
    private val actionService: ActionService? = null

    @Autowired
    private val userService: UserService? = null

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addMoney() {
        var user = userService!!.getUserByLogin("admin")
        val money = user.money
        actionService!!.applyCommands(user.id, Map.of("money", listOf("1000")))
        user = userService.getUserByLogin("admin")
        Assertions.assertEquals(money + 1000, user.money as Int)
    }

    @AfterEach
    fun resetUser() {
        val user = userService!!.getUserByLogin("admin")
        user.reset()
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addRelation() {
        var user = userService!!.getUserByLogin("admin")
        actionService!!.applyCommands(user.id, Map.of("add", listOf("relation_1:50")))
        user = userService.getUserByLogin("admin")
        for (gang in Gang.entries) {
            if (gang == Gang.BANDITS) continue
            Assertions.assertEquals(0, user.gangRelation.getRelation(gang))
        }
        Assertions.assertEquals(50, user.gangRelation.bandits)
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun removeRelation() {
        var user = userService!!.getUserByLogin("admin")
        actionService!!.applyCommands(user.id, Map.of("add", listOf("relation_1:-50")))
        user = userService.getUserByLogin("admin")
        for (gang in Gang.entries) {
            if (gang == Gang.BANDITS) continue
            Assertions.assertEquals(0, user.gangRelation.getRelation(gang))
        }
        Assertions.assertEquals(0, user.gangRelation.getRelation(Gang.BANDITS))
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun setRelation() {
        var user = userService!!.getUserByLogin("admin")
        actionService!!.applyCommands(user.id, Map.of("relation", listOf("MERCENARIES", "100")))
        user = userService.getUserByLogin("admin")
        Assertions.assertEquals(100, user.gangRelation.mercenaries)
    }
}