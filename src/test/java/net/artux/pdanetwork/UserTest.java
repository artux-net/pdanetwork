package net.artux.pdanetwork;

import lombok.extern.slf4j.Slf4j;
import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.controller.rest.user.UserController;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.user.reset.ResetService;
import net.artux.pdanetwork.service.util.SecurityService;
import net.artux.pdanetwork.utills.RandomString;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
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
    private RandomString randomString = new RandomString();

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

    public List<RegisterUserDto> getRegisterUsers() {
        List<RegisterUserDto> registerUsers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            RegisterUserDto user = RegisterUserDto.builder()
                    .login("test" + i)
                    .email("test" + i + "@test.ru")
                    .avatar(String.valueOf(i % 30))
                    .name("name")
                    .nickname("nickname")
                    .password("12345678")
                    .build();

            registerUsers.add(user);
        }

        return registerUsers;
    }

    @Test
    @WithAnonymousUser
    public void registerUser() {
        Status status = userService.registerUser(getRegisterUser());
        Assertions.assertTrue(status.isSuccess());
    }

    @Test
    @WithAnonymousUser
    public void registerManyUsers() {
        for (RegisterUserDto user : getRegisterUsers()) {
            Status status = userService.registerUser(user);
            Assertions.assertTrue(status.isSuccess());
        }
        for (RegisterUserDto user : getRegisterUsers()) {
            Assertions.assertDoesNotThrow(() -> {
                userService.getUserByLogin(user.getLogin());
            });
        }
    }

    @Test
    @WithAnonymousUser
    public void login() {
        Assertions.assertTrue(securityService.isPasswordCorrect("test", "12345678"));
    }

    @Test
    @WithAnonymousUser
    public void changePassword() {
        resetService.sendResetPasswordLetter(getRegisterUser().getEmail());
        String pass = randomString.nextString();
        String token = resetService.getTokens().stream().findAny().get();
        resetService.changePassword(token, pass);
        Assertions.assertTrue(securityService.isPasswordCorrect(getRegisterUser().getLogin(), pass));
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


}