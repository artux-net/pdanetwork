package net.artux.pdanetwork.utils;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isPasswordCorrect(String email, String password) {
        UserEntity entity = userRepository.findByEmail(email).orElseThrow();
        return passwordEncoder.matches(password, entity.getPassword());
    }

}
