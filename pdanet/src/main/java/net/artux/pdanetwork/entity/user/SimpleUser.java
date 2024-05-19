package net.artux.pdanetwork.entity.user;

import net.artux.pdanetwork.models.user.enums.Role;

import java.util.UUID;

public interface SimpleUser {

    UUID getId();

    String getLogin();

    String getPassword();

    Role getRole();

}
