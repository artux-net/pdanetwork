package net.artux.pdanetwork.service.member;

import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.user.UserEntity;

import java.util.UUID;

public interface UserService {

    Status registerUser(RegisterUserDto registerUser);

    Status handleConfirmation(String token);

    UserDto resetData();

    UserEntity getMember();

    UserDto getMemberDto();

    UserEntity getMember(String base64);

    UserEntity getMember(UUID objectId);

    UserEntity getMemberByPdaId(Long id);

    UserEntity getMemberByEmail(String email);
    UserEntity getMemberByLogin(String login);

    Status editMember(RegisterUserDto user);

}
