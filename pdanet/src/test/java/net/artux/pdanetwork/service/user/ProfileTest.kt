package net.artux.pdanetwork.service.user

import lombok.extern.slf4j.Slf4j
import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.controller.rest.admin.AdminUserController
import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileTest : AbstractTest() {

    @Autowired
    private val adminUserController: AdminUserController? = null

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    @Test
    fun usersForAdmin() {
        Assertions.assertNotEquals(0, adminUserController!!.findUsers(AdminEditUserDto(), QueryPage()).totalSize)
    }
}