package net.artux.pdanetwork.service.member;

import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserDto;
import net.artux.pdanetwork.models.user.UserEntity;

import java.util.UUID;

public interface MemberService {

    Status registerUser(RegisterUser registerUser);

    Status handleConfirmation(String token);

    UserDto resetData();

    UserEntity getMember();

    UserDto getMemberDto();

    UserEntity getMember(String base64);

    UserEntity getMember(UUID objectId);

    UserEntity getMemberByPdaId(Long id);

    UserEntity getMemberByEmail(String email);

    Status editMember(RegisterUser user);

}
