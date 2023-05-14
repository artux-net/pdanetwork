package net.artux.pdanetwork.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger logger;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.getMemberByLogin(username);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.setLastLoginAt(Instant.now());
            userRepository.save(userEntity);
            UserDetails userDetails = User.builder()
                    .passwordEncoder(s -> passwordEncoder.encode(s))
                    .username(userEntity.getLogin())
                    .password(userEntity.getPassword())
                    .roles(Role.getRoles(userEntity.getRole()))
                    .build();
            return new SecurityUser(userEntity.getId(), userDetails);
        } else {
            logger.error("User with login '" + username + "' not found.");
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_MODERATOR " +
                "\n ROLE_MODERATOR > ROLE_TESTER " +
                "\n ROLE_TESTER > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
