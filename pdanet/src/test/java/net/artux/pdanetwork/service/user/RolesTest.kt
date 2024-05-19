package net.artux.pdanetwork.service.user

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.configuration.SecurityConfiguration
import net.artux.pdanetwork.controller.rest.admin.ServerStatisticController
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RolesTest : AbstractTest() {

    @Autowired
    private val serverStatisticController: ServerStatisticController? = null

    @Autowired
    private val securityConfiguration: SecurityConfiguration? = null

    @WithAnonymousUser
    @Test
    fun isStatisticClosed() {
        Assertions.assertThrows(AccessDeniedException::class.java) { serverStatisticController!!.statistic }
    }

    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    @Test
    fun isStatisticOpenForAdmins() {
        val statistic = serverStatisticController!!.statistic
        Assertions.assertNotNull(statistic)
    }

    @Test
    fun printHierarchy() {
        println(securityConfiguration!!.roleHierarchy)
    }

}