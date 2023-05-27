package net.artux.pdanetwork.service.user;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.AdminUserDto;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.user.enums.Role;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

public interface UserService {

    Status registerUser(RegisterUserDto registerUser);

    Status handleConfirmation(String token);

    UserEntity getUserById();

    UserDto getUserDto();

    UserEntity getUserById(UUID objectId);

    AdminUserDto getUserForAdminById(UUID objectId);

    UUID getCurrentId();

    UserEntity getUserByEmail(String email);

    UserEntity getUserByLogin(String login);

    AdminUserDto updateUser(UUID userId, AdminEditUserDto adminEditUserDto);

    boolean setChatBan(UUID userId);

    Status editUser(RegisterUserDto user);

    void deleteUserById(UUID id);

    ByteArrayInputStream exportMailContacts() throws IOException;

    boolean changeEmailSetting(UUID id);

    UserEntity saveUser(RegisterUserDto registerUserDto, Role role);
}
