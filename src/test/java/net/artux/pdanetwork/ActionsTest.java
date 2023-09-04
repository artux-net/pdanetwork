package net.artux.pdanetwork;

import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.user.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Order(9)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
public class ActionsTest {

    @Autowired
    private ActionService actionService;

    @Autowired
    private UserService userService;

    public RegisterUserDto getRegisterUser() {
        return RegisterUserDto.builder()
                .login("admin")
                .email("test@gmail.com")
                .avatar("0")
                .name("name")
                .nickname("nickname")
                .password("12345678")
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addMoney() {
        UserEntity user = userService.getUserByLogin("admin");
        actionService.applyCommands(user.getId(), Map.of("money", List.of("1000")));
        user = userService.getUserByLogin("admin");
        Assert.assertTrue(1500 == user.getMoney());
    }

    @Test
    public void isUserRegistered() {
        Assert.assertNotNull(userService.getUserByLogin("admin"));
    }

}