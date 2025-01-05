package net.artux.pdanetwork.service.email;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void askForPassword(UserEntity user, String token);

    @Async
    void sendRegisterLetter(UserEntity user);

    @Async
    void sendConfirmLetter(RegisterUserDto user, String token);

}
