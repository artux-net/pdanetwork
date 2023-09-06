package net.artux.pdanetwork.service.user;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String LOGIN_VALIDATION_REGEX = "^[a-zA-Z0-9-_.]+$";
    private static final String NAME_VALIDATION_REGEX = "^[A-Za-z\u0400-\u052F']*$";
    private static final String PASSWORD_VALIDATION_REGEX = "^[A-Za-z\\d!@#$%^&*()_+№\";:?><\\[\\]{}]*$";
    private String blockedNicknames;

    @PostConstruct
    private void initBlockedNicknames() throws IOException {
        Resource resource = new ClassPathResource("/config/forbidden_nicks.txt");
        InputStream in = resource.getInputStream();
        String nicknamesFile = IOUtils.toString(in, StandardCharsets.UTF_8);
        blockedNicknames = nicknamesFile.toLowerCase(Locale.ROOT);
    }

    public Status checkUser(RegisterUserDto user) {
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
        if (!Character.isLetter(login.charAt(0))) {
            return new Status(false, "Логин должен начинаться с буквы.");
        }
        Collection<String> defectSymbols = checkStringSymbolsByRegexp(login, LOGIN_VALIDATION_REGEX);
        if (!defectSymbols.isEmpty()) {
            return new Status(false,
                    "Логин содержит запрещённые символы: " + String.join(", ", defectSymbols));
        }
        if (login.length() < 4 || login.length() > 16) {
            return new Status(false, "Логин должен содержать не менее 4 и не более 16 символов.");
        }
        if (userRepository.findByLogin(login).isPresent()) {
            return new Status(false, "Пользователь с таким логином уже существует.");
        }
        return new Status(true);
    }

    private Status checkName(String name) {
        if (!StringUtils.hasText(name)) {
            return new Status(false, "Имя не может быть пустым.");
        }
        Collection<String> defectSymbols = checkStringSymbolsByRegexp(name, NAME_VALIDATION_REGEX);
        if (!defectSymbols.isEmpty()) {
            return new Status(false,
                    "Имя содержит запрещённые символы: " + String.join(", ", defectSymbols));
        }
        if (name.length() < 2 || name.length() > 16) {
            return new Status(false, "Имя должно содержать не менее 2 и не более 16 символов.");
        }
        return new Status(true);
    }

    private Status checkNickname(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            return new Status(false, "Прозвище не может быть пустым.");
        }
        Collection<String> defectSymbols = checkStringSymbolsByRegexp(nickname, NAME_VALIDATION_REGEX);
        if (!defectSymbols.isEmpty()) {
            return new Status(false,
                    "Прозвище содержит запрещённые символы: " + String.join(", ", defectSymbols));
        }
        if (nickname.length() < 2 || nickname.length() > 16) {
            return new Status(false, "Прозвище должно содержать не менее 2 и не более 16 символов.");
        }
        if (blockedNicknames.contains(nickname.toLowerCase(Locale.ROOT)))
            return new Status(false, "Прозвище должно быть уникальным.");
        return new Status(true);
    }

    private Status checkPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return new Status(false, "Пароль не может быть пустым.");
        }
        Collection<String> defectSymbols = checkStringSymbolsByRegexp(password, PASSWORD_VALIDATION_REGEX);
        if (!defectSymbols.isEmpty()) {
            return new Status(false,
                    "Пароль содержит запрещённые символы: " + String.join(", ", defectSymbols));
        }
        if (password.length() < 8 || password.length() > 24) {
            return new Status(false, "Пароль должен содержать не менее 8 и не более 24 символов.");
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
        if (userRepository.findMemberByEmail(email).isPresent()) {
            return new Status(false, "Пользователь с таким e-mail уже существует.");
        }
        return new Status(true);
    }

    private Collection<String> checkStringSymbolsByRegexp(String str, String regexp) {
        if (!StringUtils.hasText(str)) {
            return Collections.emptyList();
        }
        Collection<String> result = new ArrayList<>();
        for (char chr : str.toCharArray()) {
            String chrOfStr = Character.toString(chr);
            if (!chrOfStr.matches(regexp)) {
                result.add(chrOfStr);
            }
        }
        return result;
    }
}
