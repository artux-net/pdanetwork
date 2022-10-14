package net.artux.pdanetwork.service.user.reset;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.Security;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ResetServiceImpl implements ResetService {

    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final StoryMapper storyMapper;

    private final UserMapper userMapper;

    private final HashMap<String, String> requests = new HashMap<>();

    @Override
    public Status sendLetter(String email) {
        UserEntity userEntity = userService.getUserByEmail(email);
        //TODO
        if (!requests.containsValue(userEntity.getEmail())) {
            String token = Security.encrypt(userEntity.getPassword() + userEntity.getLogin());
            try {
                emailService.askForPassword(userEntity, token);
                addCurrent(token, userEntity);
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
        new Timer(30 * 60 * 1000, e -> requests.remove(token)).start();
    }

    @Override
    public Status changePassword(String token, String password) {
        UserEntity userEntity = userService.getUserByEmail(requests.get(token));
        RegisterUserDto registerUser = userMapper.regUser(userEntity);
        registerUser.setPassword(password);
        return userService.editUser(registerUser);
    }

    @Override
    @Transactional
    public StoryData resetData() {
        UserEntity userEntity = userService.getUserById();
        userEntity.reset();
        userEntity.getStoryStates().clear();
        return storyMapper.storyData(userRepository.saveAndFlush(userEntity));
    }

}
