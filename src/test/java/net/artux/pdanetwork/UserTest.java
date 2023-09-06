package net.artux.pdanetwork;

import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.controller.rest.user.UserController;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.reset.ResetService;
import net.artux.pdanetwork.service.util.SecurityService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
public class UserTest {

    @Autowired
    private UserController userController;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private ResetService resetService;

    public RegisterUserDto getRegisterUser() {
        return RegisterUserDto.builder()
                .login("test")
                .email("prygunovmaksim@yandex.ru")
                .avatar("0")
                .name("test")
                .nickname("test")
                .password("12345678")
                .build();
    }

    @Test
    @WithAnonymousUser
    @Disabled // TODO remove after email fix
    public void registerUser() {
        Status status = userService.registerUser(getRegisterUser());
        System.out.println(status.getDescription());
        Assertions.assertTrue(status.isSuccess());
    }

    @Test
    @WithAnonymousUser
    public void login() {
        Assertions.assertTrue(securityService.isPasswordCorrect("test", "12345678"));
    }

    @Test
    @WithAnonymousUser
    public void changePassword() {
        resetService.sendResetPasswordLetter("test@gmail.com");
        Assertions.assertTrue(securityService.isPasswordCorrect("test", "12345678"));
    }

    @Test
    public void isUserRegistered() {
        Assert.assertNotNull(userService.getUserByLogin("test"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void isAdminCreated() {
        StoryData data = actionService.applyCommands(Collections.emptyMap());
        Assertions.assertEquals("admin", data.getLogin());
    }

    @Test
    @WithAnonymousUser
    public void changePassword() {
        resetService.sendResetPasswordLetter(getRegisterUser().getEmail());

        StoryData data = actionService.applyCommands(Collections.emptyMap());
        Assertions.assertEquals("admin", data.getLogin());
    }
}