package net.artux.pdanetwork;

import lombok.extern.slf4j.Slf4j;
import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Map;

@Order(10)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class ActionsTest {

    @Autowired
    private ActionService actionService;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addMoney() {
        UserEntity user = userService.getUserByLogin("admin");
        int money = user.getMoney();
        actionService.applyCommands(user.getId(), Map.of("money", List.of("1000")));
        user = userService.getUserByLogin("admin");
        Assertions.assertEquals(money + 1000, (int) user.getMoney());
    }

    @AfterEach
    public void resetUser() {
        UserEntity user = userService.getUserByLogin("admin");
        user.reset();
        log.info("User reset");
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addRelation() {
        UserEntity user = userService.getUserByLogin("admin");
        actionService.applyCommands(user.getId(), Map.of("add", List.of("relation_1:50")));
        user = userService.getUserByLogin("admin");
        for (Gang gang : Gang.values()) {
            if (gang == Gang.BANDITS)
                continue;
            Assertions.assertEquals(0, user.getGangRelation().getRelation(gang));
        }
        Assertions.assertEquals(50, user.getGangRelation().getBandits());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void removeRelation() {
        UserEntity user = userService.getUserByLogin("admin");
        actionService.applyCommands(user.getId(), Map.of("add", List.of("relation_1:-50")));
        user = userService.getUserByLogin("admin");
        for (Gang gang : Gang.values()) {
            if (gang == Gang.BANDITS)
                continue;
            Assertions.assertEquals(0, user.getGangRelation().getRelation(gang));
        }
        Assertions.assertEquals(0, user.getGangRelation().getRelation(Gang.BANDITS));
    }


    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void setRelation() {
        UserEntity user = userService.getUserByLogin("admin");
        actionService.applyCommands(user.getId(), Map.of("relation", List.of("MERCENARIES", "100")));
        user = userService.getUserByLogin("admin");
        Assertions.assertEquals(100, user.getGangRelation().getMercenaries());
    }

}