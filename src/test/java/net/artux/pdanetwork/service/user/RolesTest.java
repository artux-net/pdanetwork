package net.artux.pdanetwork.service.user;

import net.artux.pdanetwork.configuration.SecurityConfiguration;
import net.artux.pdanetwork.controller.rest.admin.StatisticController;
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
    private StatisticController statisticController;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Test
    @WithAnonymousUser
    public void isStatisticClosed() {
        assertThrows(AccessDeniedException.class, () -> statisticController.getStatistic());
    }

    @Test
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void isStatisticOpenForAdmins() {
        StatisticDto statistic = statisticController.getStatistic();
        assertNotNull(statistic);
    }

    @Test
    public void printHierarchy() {
        System.out.println(securityConfiguration.getRoleHierarchy());
    }

}