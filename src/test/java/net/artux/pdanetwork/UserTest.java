package net.artux.pdanetwork;

import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.service.user.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
public class UserTest {

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
    public void registerUser() {
        Status status = userService.registerUser(getRegisterUser());
        System.out.println(status);
        Assert.assertTrue(status.isSuccess());
    }

    @Test
    public void isUserRegistered() {
        Assert.assertNotNull(userService.getUserByLogin("admin"));
    }

}