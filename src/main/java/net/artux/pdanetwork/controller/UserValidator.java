package net.artux.pdanetwork.controller;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final MemberRepository memberRepository;

    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public Status checkUser(RegisterUser user) {
        return Stream.of(checkLogin(user.getLogin()),
                        checkName(user.getName()),
                        checkNickname(user.getNickname()),
                        checkPassword(user.getPassword()),
                        checkEmail(user.getEmail()))
                .filter(status -> !status.isSuccess())
                .findFirst()
                .orElse(new Status(true, "Логин и почта свободны."));
    }

    private Status checkLogin(String login) {
        if (!StringUtils.hasText(login)) {
            return new Status(false, "Логин не может быть пустым.");
        }
        if (login.trim().length() < 4 || login.trim().length() > 16) {
            return new Status(false, "Логин должен содержать не менее 4 и не более 16 символов.");
        }
        if (memberRepository.getMemberByLogin(login.trim()).isPresent()) {
            return new Status(false, "Пользователь с таким логином уже существует.");
        }
        return new Status(true);
    }

    private Status checkName(String name) {
        if (!StringUtils.hasText(name)) {
            return new Status(false, "Имя не может быть пустым.");
        }
        if (name.trim().length() < 2 || name.trim().length() > 16) {
            return new Status(false, "Имя должно содержать не менее 2 и не более 16 символов.");
        }
        return new Status(true);
    }

    private Status checkNickname(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            return new Status(false, "Кличка не может быть пустой.");
        }
        if (nickname.trim().length() < 2 || nickname.trim().length() > 16) {
            return new Status(false, "Кличка должна содержать не менее 2 и не более 16 символов.");
        }
        return new Status(true);
    }

    private Status checkPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return new Status(false, "Пароль не может быть пустым.");
        }
        if (password.trim().length() < 8) {
            return new Status(false, "Пароль должен содержать не менее 8 символов.");
        }
        return new Status(true);
    }

    private Status checkEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return new Status(false, "Почта не может быть пустой.");
        }
        if (!email.matches(EMAIL_VALIDATION_REGEX)) {
            return new Status(false, "Почта не существует.");
        }
        if (memberRepository.getMemberByEmail(email).isPresent()) {
            return new Status(false, "Пользователь с таким e-mail уже существует.");
        }
        return new Status(true);
    }
}
