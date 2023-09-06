package net.artux.pdanetwork.service.util;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isPasswordCorrect(String login, String password) {
        UserEntity entity = userRepository.findByLogin(login).orElseThrow();
        return entity.getPassword().equals(passwordEncoder.encode(password));
    }

}
