package net.artux.pdanetwork.service.member;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.controller.UserValidator;
import net.artux.pdanetwork.models.MemberMapper;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserDto;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.user.UsersRepository;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.util.Utils;
import net.artux.pdanetwork.utills.Security;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UsersRepository usersRepository;
    private final EmailService emailService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final Logger logger;

    private static final Map<String, RegisterUser> registerUserMap = new HashMap<>();

    @Override
    public Status registerUser(RegisterUser newUser) {
        Status status = userValidator.checkUser(newUser);

        String token = Security.encrypt(newUser.getLogin());

        if (status.isSuccess()) {
            if (!registerUserMap.containsKey(token))
                try {
                    emailService.sendConfirmLetter(newUser, token);
                    addCurrent(token, newUser);
                    status = new Status(true, "Проверьте почту.");
                } catch (Exception e) {
                    status = new Status(false, "Не удалось отправить письмо на " + newUser.getEmail());
                }
            else
                status = new Status(false, "Пользователь ожидает регистрации, проверьте почту.");
        }

        return status;
    }

    private void addCurrent(String token, RegisterUser user) {
        logger.info("Add to register wait list with token: " + token + ", " + user.getEmail());
        registerUserMap.put(token, user);
        new Timer(30 * 60 * 1000, e -> registerUserMap.remove(token)).start();
    }

    public Status handleConfirmation(String token) {
        if (registerUserMap.containsKey(token)) {
            UserEntity member = usersRepository.save(new UserEntity(registerUserMap.get(token), passwordEncoder));
            long pdaId = member.getId();
            logger.info("User" + member.getLogin() + " registered.");
            try {
                emailService.sendRegisterLetter(registerUserMap.get(token), pdaId);
                registerUserMap.remove(token);
                return new Status(true, pdaId + " - Это ваш pdaId, мы вас зарегистрировали, спасибо!");
            } catch (Exception e) {
                return new Status(true, "Не получилось отправить подтверждение на почту, но мы вас зарегистрировали, спасибо!");
            }
        } else return new Status(false, "Ссылка устарела или не существует");
    }

    @Override
    public UserDto resetData() {
        UserEntity userEntity = getMember();
        userEntity.setMoney(500);
        //userEntity.setData(new Data());
        usersRepository.save(userEntity);
        return memberMapper.memberDto(userEntity);
    }

    @Override
    public UserEntity getMember() {
        return usersRepository.getMemberByLogin(SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователя не существует"));
    }

    @Override
    public UserDto getMemberDto() {
        return memberMapper.memberDto(getMember());
    }

    @Override
    public UserEntity getMember(String base64) {
        if (Utils.isEmpty(base64))
            return null;
        base64 = base64.replaceFirst("Basic ", "");
        base64 = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        String login = base64.split(":")[0];
        String password = base64.split(":")[1];
        Optional<UserEntity> optionalMember = usersRepository.getMemberByLogin(login);
        if (optionalMember.isPresent()
                && passwordEncoder.matches(password, optionalMember.get().getPassword()))
            return optionalMember.get();
        else return null;
    }

    @Override
    public UserEntity getMember(UUID objectId) {
        return usersRepository.getByUid(objectId).orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }

    @Override
    public UserEntity getMemberByPdaId(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }

    @Override
    public UserEntity getMemberByEmail(String email) {
        return usersRepository.getMemberByEmail(email).orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }


    @Override
    public Status editMember(RegisterUser user) {
        UserEntity userEntity = getMember();
        Status status = userValidator.checkUser(user);
        if (status.isSuccess()) {
            userEntity.setName(user.getName());
            userEntity.setNickname(user.getNickname());
            userEntity.setLogin(user.getLogin());
            //TODO email may be wrong
            userEntity.setEmail(user.getEmail());
            userEntity.setAvatar(user.getAvatar());
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepository.save(userEntity);
            status.setDescription("Данные изменены");
        }

        return status;
    }


}
