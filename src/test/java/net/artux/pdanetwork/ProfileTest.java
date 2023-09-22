package net.artux.pdanetwork;

import lombok.extern.slf4j.Slf4j;
import net.artux.pdanetwork.controller.rest.admin.AdminUserController;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileTest {

    @Autowired
    private AdminUserController adminUserController;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void getUsersForAdmin() {
        Assertions.assertNotEquals(0, adminUserController.findUsers(new AdminEditUserDto(), new QueryPage()).getTotalSize());
    }

}