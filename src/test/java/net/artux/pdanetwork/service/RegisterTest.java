package net.artux.pdanetwork.service;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.service.member.UserService;
import net.artux.pdanetwork.service.member.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterTest {

    @Autowired
    private UserService userService;

    @Test
    public void registerUser() {
        RegisterUserDto registerUser = new RegisterUserDto();
        registerUser.setLogin("maxxx");
        registerUser.setNickname("maaa");
        registerUser.setAvatar("2");
        registerUser.setEmail("s@a.r");
        registerUser.setPassword("12345678");
        registerUser.setName("name");
        Status s = userService.registerUser(registerUser);
        assertThat(s.isSuccess()).isTrue();
    }

    @Test
    public void confirmUser() {
        String token = (String) UserServiceImpl.getRegisterUserMap().keySet().toArray()[0];
        Status s = userService.handleConfirmation(token);
        assertThat(s.isSuccess()).isTrue();
    }

}
