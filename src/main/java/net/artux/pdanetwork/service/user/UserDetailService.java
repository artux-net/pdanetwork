package net.artux.pdanetwork.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger logger;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.getMemberByLogin(username);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.setLastLoginAt(Instant.now());
            userRepository.save(userEntity);
            return new SecurityUser(userEntity.getLogin(), userEntity.getPassword(), getAuthorities(userEntity.getRole()), userEntity.getId());
        } else {
            logger.error("User with login '" + username + "' not found.");
            throw new UsernameNotFoundException("User not found");
        }
    }

    public List<SimpleGrantedAuthority> getAuthorities(Role userRole){
        int priority = userRole.getPriority();
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();

        for (Role role : Role.values()){
            if (role.getPriority() <= priority)
                authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }
}
