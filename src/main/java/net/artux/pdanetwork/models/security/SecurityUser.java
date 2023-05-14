package net.artux.pdanetwork.models.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;


@Getter
public class SecurityUser extends User {

    private final UUID id;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UUID id) {
        super(username, password, authorities);
        this.id = id;
    }

    public SecurityUser(UUID id, UserDetails details) {
        super(details.getUsername(), details.getPassword(), details.getAuthorities());
        this.id = id;
    }
}
