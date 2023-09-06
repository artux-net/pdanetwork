package net.artux.pdanetwork.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto;
import net.artux.pdanetwork.models.user.dto.AdminUserDto;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.utills.RandomString;
import net.artux.pdanetwork.utills.security.AdminAccess;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final Map<String, RegisterUserDto> registerUserMap = new HashMap<>();
    private final Timer timer = new Timer();
    private final Environment environment;
    private final RandomString randomString = new RandomString();

    private boolean emailConfirmation = false;

    @Override
    public Status registerUser(RegisterUserDto newUser) {
        Status status = userValidator.checkUser(newUser);
        if (status.isSuccess()) {
            if (!registerUserMap.containsValue(newUser))
                try {
                    String token = generateToken(newUser);
                    if (emailConfirmation) {
                        emailService.sendConfirmLetter(newUser, token);
                        status = new Status(true, "Проверьте почту.");
                    }else {
                        handleConfirmation(token);
                        status = new Status(true, "Учетная запись зарегистрирована.");
                    }

                } catch (Exception e) {
                    logger.error("Registration", e);
                    status = new Status(false, "Не удалось отправить письмо на " + newUser.getEmail());
                }
            else
                status = new Status(false, "Пользователь ожидает регистрации, проверьте почту.");
        }

        return status;
    }

    private String generateToken(RegisterUserDto user) {
        String token = randomString.nextString();
        logger.info("Пользователь {} добавлен в лист ожидания регистрации с токеном {}, токен возможно использовать через сваггер.", user.getEmail(), token);
        registerUserMap.put(token, user);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                registerUserMap.remove(token);
            }
        }, 10 * 60 * 1000);
        return token;
    }

    public Status handleConfirmation(String token) {
        if (registerUserMap.containsKey(token)) {
            RegisterUserDto regDto = registerUserMap.get(token);
            Status currentStatus = userValidator.checkUser(regDto);
            registerUserMap.remove(token);
            if (!currentStatus.isSuccess())
                return currentStatus;

            UserEntity member = saveUser(regDto, Role.USER);
            long pdaId = member.getPdaId();
            logger.info("Пользователь {} ({} {}) зарегистрирован.", member.getLogin(), member.getName(), member.getNickname());
            try {
                emailService.sendRegisterLetter(regDto, pdaId);
                return new Status(true, pdaId + " - Это ваш pdaId, мы вас зарегистрировали, спасибо!");
            } catch (Exception e) {
                logger.error("Handle confirmation", e);
                return new Status(true, "Не получилось отправить подтверждение на почту, но мы вас зарегистрировали, спасибо!");
            }
        } else return new Status(false, "Ссылка устарела или не существует");
    }

    public UserEntity saveUser(RegisterUserDto registerUserDto, Role role) {
        return userRepository.save(new UserEntity(registerUserDto, passwordEncoder, role));
    }

    public UUID getCurrentId() {
        return ((SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getId();
    }

    @Override
    public UserEntity getUserById() {
        UserEntity userEntity = userRepository.findById(getCurrentId()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        if (userEntity.getLastLoginAt().plusSeconds(300).isBefore(Instant.now())) {
            userEntity.setLastLoginAt(Instant.now());
            return userRepository.save(userEntity);
        } else return userEntity;
    }

    @Override
    public UserDto getUserDto() {
        return userMapper.dto(getUserById());
    }

    @Override
    public UserEntity getUserById(UUID objectId) {
        return userRepository.findById(objectId).orElseThrow();
    }

    @Override
    public AdminUserDto getUserForAdminById(UUID objectId) {
        return userMapper.adminDto(getUserById(objectId));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findMemberByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }

    @Override
    public UserEntity getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("Пользователя не существует"));
    }

    @Override
    @AdminAccess
    public AdminUserDto updateUser(UUID id, AdminEditUserDto adminEditUserDto) {
        UserEntity user = getUserById(id);
        user.setName(adminEditUserDto.getName());
        user.setNickname(adminEditUserDto.getNickname());
        user.setLogin(adminEditUserDto.getLogin());
        user.setEmail(adminEditUserDto.getEmail());
        user.setAvatar(adminEditUserDto.getAvatar());

        user.setRole(adminEditUserDto.getRole());
        user.setGang(adminEditUserDto.getGang());
        user.setChatBan(adminEditUserDto.getChatBan());
        logger.info("Пользователь {} обновлен модератором {}", userMapper.dto(user), getUserById().getLogin());

        return userMapper.adminDto(userRepository.save(user));
    }

    @Override
    public boolean setChatBan(UUID userId) {
        UserEntity user = getUserById(userId);
        user.setChatBan(!user.getChatBan());
        return userRepository.save(user).getChatBan();
    }


    @Override
    public Status editUser(RegisterUserDto user) {
        UserEntity userEntity = getUserById();
        userEntity.setName(user.getName());
        userEntity.setNickname(user.getNickname());
        //userEntity.setLogin(user.getLogin());
        //TODO добавить через модуль подтверждения почту
        //userEntity.setEmail(user.getEmail());
        userEntity.setAvatar(user.getAvatar());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);

        String message = "Обновлен пользователь " + user.getLogin();
        return new Status(true, message);
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public ByteArrayInputStream exportMailContacts() throws IOException {
        return exportUsers(userRepository.findAllByReceiveEmailsTrue());
    }

    @Override
    public boolean changeEmailSetting(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.setReceiveEmails(!user.getReceiveEmails());
        return userRepository.save(user)
                .getReceiveEmails();
    }

    public ByteArrayInputStream exportUsers(List<UserEntity> users) throws IOException {
        logger.info("{} exported {} contacts.", getUserById().getLogin(), users.size());
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("users");

        CellStyle headerCellStyle = workbook.createCellStyle();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(headerCellStyle);

        cell = row.createCell(1);
        cell.setCellStyle(headerCellStyle);


        Iterator<UserEntity> userIterator = users.listIterator();
        for (int i = 0; userIterator.hasNext(); i++) {
            UserEntity contactEntity = userIterator.next();
            Row header = sheet.createRow(i);

            Cell headerCell = header.createCell(0);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(contactEntity.getEmail());

            headerCell = header.createCell(1);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(contactEntity.getLogin());

            headerCell = header.createCell(2);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(contactEntity.getName());

            headerCell = header.createCell(3);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(contactEntity.getId().toString());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
