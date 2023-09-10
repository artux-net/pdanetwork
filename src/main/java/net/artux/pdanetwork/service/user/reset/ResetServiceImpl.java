package net.artux.pdanetwork.service.user.reset;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.controller.web.user.PasswordController;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.ValuesService;
import net.artux.pdanetwork.utills.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class ResetServiceImpl implements ResetService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final StoryMapper storyMapper;
    private final ValuesService valuesService;
    private final Logger logger = LoggerFactory.getLogger(ResetServiceImpl.class);
    private final RandomString randomString = new RandomString();
    private final Timer timer = new Timer();
    private final HashMap<String, String> requests = new HashMap<>();

    @Override
    public Status sendResetPasswordLetter(String email) {
        UserEntity userEntity = userService.getUserByEmail(email);
        if (!requests.containsValue(userEntity.getEmail())) {
            String token = randomString.nextString();
            logger.info("Ссылка для сброса пароля {} для пользователя: {}",
                    valuesService.getAddress() + PasswordController.RESET_PASSWORD_URL + "?t=" + token,
                    userEntity.getLogin());
            addCurrent(token, userEntity);
            if (!valuesService.isEmailConfirmationEnabled())
                return new Status(false, "Свяжитесь с администратором для сброса пароля");

            try {
                emailService.askForPassword(userEntity, token);
                return new Status(true, "Мы отправили письмо с паролем на Вашу почту");
            } catch (Exception e) {
                return new Status(false, e.getMessage());
            }
        } else {
            return new Status(false, "Такого пользователя не существует, либо письмо уже отправлено");
        }
    }

    private void addCurrent(String token, UserEntity user) {
        requests.put(token, user.getEmail());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requests.remove(token);
            }
        }, 10 * 60 * 1000);
    }

    @Override
    public Status changePassword(String token, String password) {
        String email = requests.get(token);
        if (email == null)
            return new Status(false, "Токен не найден");

        UserEntity userEntity = userService.getUserByEmail(email);

        logger.info("Изменение пароля для пользователя: {}", userEntity.getLogin());
        logger.info("Хэш старого пароля: {}", userEntity.getPassword());

        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity = userRepository.save(userEntity);

        logger.info("Хэш нового пароля: {}", userEntity.getPassword());

        return new Status(true, "Пароль успешно изменен");
    }

    @Override
    @Transactional
    public StoryData resetData() {
        UserEntity userEntity = userService.getUserById();
        userEntity.reset();
        userEntity.getStoryStates().clear();
        return storyMapper.storyData(userRepository.saveAndFlush(userEntity));
    }

    @Override
    public Collection<String> getTokens() {
        return requests.keySet();
    }

}
