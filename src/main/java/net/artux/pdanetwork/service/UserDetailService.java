package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.user.UsersRepository;
import org.slf4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final Logger logger;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = usersRepository.getMemberByLogin(username);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().toString()));

            return new User(userEntity.getLogin(), userEntity.getPassword(), authorities);
        } else {
            logger.error("User with login '" + username + "' not found.");
            throw new UsernameNotFoundException("User not found");
        }
    }
}
