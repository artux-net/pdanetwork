package net.artux.pdanetwork.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.SimpleUser;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger logger;

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


}
