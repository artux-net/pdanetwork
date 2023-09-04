package net.artux.pdanetwork;

import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.user.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ActionService actionService;

    public RegisterUserDto getRegisterUser() {
        return RegisterUserDto.builder()
                .login("test")
                .email("test@gmail.com")
                .avatar("0")
                .name("test")
                .nickname("test")
                .password("12345678")
                .build();
    }

    @Test
    public void registerUser() {
        Status status = userService.registerUser(getRegisterUser());
        System.out.println(status.getDescription());
        //Assertions.assertTrue(status.isSuccess());
    }

    @Test
    public void isUserRegistered() {
        Assert.assertNotNull(userService.getUserByLogin("test"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void isAdminCreated() {
        StoryData data = actionService.applyCommands(Collections.emptyMap());
        Assertions.assertNotNull(data.getLogin().equals("admin"));
    }

}