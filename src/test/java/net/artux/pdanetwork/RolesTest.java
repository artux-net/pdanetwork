package net.artux.pdanetwork;

import net.artux.pdanetwork.controller.rest.admin.ServerStatisticController;
import net.artux.pdanetwork.models.statistic.StatisticDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RolesTest {

    @Autowired
    private ServerStatisticController serverStatisticController;

    @Test
    @WithAnonymousUser
    public void isStatisticClosed() {
        assertThrows(AccessDeniedException.class, () -> serverStatisticController.getStatistic());
    }

    @Test
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void isStatisticOpenForAdmins() {
        StatisticDto statistic = serverStatisticController.getStatistic();
        assertNotNull(statistic);
    }

}