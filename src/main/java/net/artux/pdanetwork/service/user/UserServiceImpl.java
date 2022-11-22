package net.artux.pdanetwork.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.utills.Security;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final Logger logger;
    private final Map<String, RegisterUserDto> registerUserMap;
    private final Timer timer = new Timer();
    private final Environment environment;

    @PostConstruct
    private void registerFirstUsers() {
        String email = environment.getProperty("administrator.email");
        String login = environment.getProperty("administrator.login");
        String name = environment.getProperty("administrator.name");
        String nickname = environment.getProperty("administrator.nickname");

        String password = RandomStringUtils.randomAlphabetic(16);

        if (userRepository.count() < 1) {
            saveUser(RegisterUserDto.builder()
                    .email(email)
                    .login(login)
                    .name(name)
                    .password(password)
                    .nickname(nickname)
                    .avatar("1").build(), Role.ADMIN);
            logger.info("""
                    There are no users, the first user with a admin role created.\s
                     Email: {}\s
                     Login: {}\s
                     Password: {}\s
                    """, email, login, password);
        }

    }

    @Override
    public Status registerUser(RegisterUserDto newUser) {
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

    private void addCurrent(String token, RegisterUserDto user) {
        logger.info("Add to register wait list with token: " + token + ", " + user.getEmail());
        registerUserMap.put(token, user);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                registerUserMap.remove(token);
            }
        }, 30 * 60 * 1000);
    }

    public Status handleConfirmation(String token) {
        if (registerUserMap.containsKey(token)) {
            UserEntity member = saveUser(registerUserMap.get(token), Role.USER);
            long pdaId = member.getPdaId();
            logger.info("User PDA#" + member.getLogin() + " registered.");
            registerUserMap.remove(token);
            try {
                emailService.sendRegisterLetter(registerUserMap.get(token), pdaId);
                return new Status(true, pdaId + " - Это ваш pdaId, мы вас зарегистрировали, спасибо!");
            } catch (Exception e) {
                return new Status(true, "Не получилось отправить подтверждение на почту, но мы вас зарегистрировали, спасибо!");
            }
        } else return new Status(false, "Ссылка устарела или не существует");
    }

    private UserEntity saveUser(RegisterUserDto registerUserDto, Role role) {
        return userRepository.save(new UserEntity(registerUserDto, passwordEncoder, role));
    }

    public UUID getCurrentId() {
        return ((SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getId();
    }

    @Override
    public UserEntity getUserById() {
        return userRepository.findById(getCurrentId()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Override
    public UserDto getUserDto() {
        return userMapper.dto(getUserById());
    }

    @Override
    public UserEntity getUserById(UUID objectId) {
        return userRepository.getById(objectId);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findMemberByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }

    @Override
    public UserEntity getUserByLogin(String login) {
        return userRepository.getMemberByLogin(login).orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }

    @Override
    public UserEntity updateByAdmin(UUID id, AdminEditUserDto adminEditUserDto) {
        UserEntity user = getUserById(id);
        user.setRole(adminEditUserDto.getRole());
        return userRepository.save(user);
    }


    @Override
    public Status editUser(RegisterUserDto user) {
        UserEntity userEntity = getUserById();
        Status status = userValidator.checkUser(user);
        if (status.isSuccess()) {
            userEntity.setName(user.getName());
            userEntity.setNickname(user.getNickname());
            userEntity.setLogin(user.getLogin());
            //TODO email may be wrong
            userEntity.setEmail(user.getEmail());
            userEntity.setAvatar(user.getAvatar());
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userEntity);
            status.setDescription("Данные изменены");
        }

        return status;
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }


}
