package net.artux.pdanetwork.service.user;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.SimpleUser;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.utills.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomString randomString;
    private final Environment environment;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isBlank())
            throw new UsernameNotFoundException("Access denied.");

        Optional<SimpleUser> userOptional = userRepository.getByLogin(username);
        if (userOptional.isPresent()) {
            SimpleUser simpleUser = userOptional.get();
            UserDetails userDetails = User.builder()
                    .username(simpleUser.getLogin())
                    .password(simpleUser.getPassword())
                    .authorities("ROLE_" + simpleUser.getRole().name())
                    .build();
            return new SecurityUser(simpleUser.getId(), userDetails);
        } else {
            logger.error("User with login '" + username + "' not found.");
            throw new UsernameNotFoundException("User not found");
        }
    }

    @PostConstruct
    private void registerFirstUsers() {
        String email = environment.getProperty("administrator.email");
        String login = environment.getProperty("administrator.login");
        String name = environment.getProperty("administrator.name");
        String nickname = environment.getProperty("administrator.nickname");

        String password = randomString.nextString();

        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email(email)
                .login(login)
                .name(name)
                .password(password)
                .nickname(nickname)
                .avatar("1")
                .build();

        if (userRepository.count() < 1) {
            userRepository.save(new UserEntity(registerUserDto, passwordEncoder, Role.ADMIN));
            logger.info("""
                    There are no users, the first user with a admin role created.\s
                     Email: {}\s
                     Login: {}\s
                     Password: {}\s
                    """, email, login, password);
        }

    }

}
