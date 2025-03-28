package net.artux.pdanetwork.service.user

import jakarta.mail.MessagingException
import mu.KLogging
import net.artux.pdanetwork.entity.mappers.UserMapper
import net.artux.pdanetwork.entity.security.SecurityUser
import net.artux.pdanetwork.entity.user.UserConfirmationEntity
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.exception.UserNotFoundException
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto
import net.artux.pdanetwork.models.user.dto.AdminUserDto
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.models.user.dto.UserDto
import net.artux.pdanetwork.models.user.enums.Role
import net.artux.pdanetwork.repository.user.UserConfirmationRepository
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.email.EmailService
import net.artux.pdanetwork.service.util.ValuesService
import net.artux.pdanetwork.utils.RandomString
import net.artux.pdanetwork.utils.security.AdminAccess
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.context.MessageSource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.util.Locale
import java.util.UUID

@Service
@Transactional
@Suppress("LongParameterList", "TooManyFunctions")
open class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userConfirmationRepository: UserConfirmationRepository,
    private val emailService: EmailService,
    private val valuesService: ValuesService,
    private val userValidator: UserValidator,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper,
    private val messageSource: MessageSource
) : UserService {

    private val randomString = RandomString()

    @Transactional
    override fun registerUser(registerUser: RegisterUserDto): Status {
        val status = userValidator.checkUser(registerUser)
        if (!status.isSuccess) {
            return status
        }
        return try {
            val createdUserEntity = saveUser(registerUser, INITIAL_ROLE)
            val token = generateConfirmationToken(createdUserEntity)

            emailService.sendConfirmLetter(registerUser, token)
            Status(
                true,
                """
                    Учетная запись зарегистрирована, но лучше ее подтвердить переходом по ссылке из письма,
                    которое было отравлено на ${registerUser.email}. Можете выполнить вход по этому email.
                """.trimIndent()
            )
        } catch (e: MessagingException) {
            logger.error("Registration", e)
            Status(false, "Не удалось отправить письмо на " + registerUser.email)
        }
    }

    private fun generateConfirmationToken(user: UserEntity): String {
        val token = randomString.nextString()
        logger.info(
            "Пользователь {} добавлен в базу с токеном подтверждения {}",
            user.email,
            token
        )

        logger.info("Ссылка подтверждения аккаунта: " + valuesService.address + "/confirmation/register?t=" + token)
        userConfirmationRepository.save(
            UserConfirmationEntity().apply {
                this.user = user
                this.token = token
            }
        )
        return token
    }

    @Transactional
    override fun handleConfirmation(token: String): Status {
        val confirmationEntity = userConfirmationRepository.findByToken(token)
        return if (confirmationEntity.isPresent) {
            val user = userRepository.findById(confirmationEntity.get().id).orElseThrow()
            user.isConfirmed = true
            userRepository.save(user)
            userConfirmationRepository.deleteById(user.id)

            logger.info("Пользователь {} ({} {}) подтвержден.", user.login, user.name, user.nickname)
            try {
                emailService.sendRegisterLetter(user)
                Status(true, "${user.pdaId} - Это ваш pdaId, мы вас зарегистрировали, спасибо!")
            } catch (e: MessagingException) {
                logger.error("Handle confirmation", e)
                Status(
                    true,
                    "Не получилось отправить подтверждение на почту, но мы вас зарегистрировали, спасибо!"
                )
            }
        } else {
            Status(false, "Ссылка устарела или не существует")
        }
    }

    private fun saveUser(registerUserDto: RegisterUserDto, role: Role): UserEntity {
        return userRepository.save(UserEntity(registerUserDto, passwordEncoder, role))
    }

    override fun getUsersByIds(ids: Collection<UUID>): List<UserEntity> {
        return userRepository.findAllById(ids)
    }

    override fun getCurrentId(): UUID {
        val securityUser = SecurityContextHolder.getContext().authentication.principal as SecurityUser
        return securityUser.id
    }

    override fun getCurrentUser(): UserEntity {
        val userEntity = userRepository.findById(getCurrentId()).orElseThrow {
            UserNotFoundException("Пользователь не найден")
        }

        val lastOnlineAt = userEntity.lastLoginAt.plus(UPDATE_ONLINE_DURATION)
        return if (lastOnlineAt.isBefore(Instant.now())) {
            updateLastLoginAt(userEntity)
        } else {
            userEntity
        }
    }

    @Transactional
    open fun updateLastLoginAt(userEntity: UserEntity): UserEntity {
        userEntity.lastLoginAt = Instant.now()
        return userRepository.save(userEntity)
    }

    override fun getUserDto(): UserDto {
        return userMapper.dto(getCurrentUser())
    }

    override fun getCurrentUser(objectId: UUID): UserEntity {
        return userRepository.findById(objectId).orElseThrow()
    }

    override fun getUserForAdminById(objectId: UUID): AdminUserDto {
        return userMapper.adminDto(getCurrentUser(objectId))
    }

    override fun getUserByEmail(email: String): UserEntity {
        return userRepository.findByEmail(email).orElseThrow { RuntimeException("Пользователя не существует") }
    }

    override fun getUserByLogin(login: String): UserEntity {
        return userRepository.findByLogin(login).orElseThrow { RuntimeException("Пользователя не существует") }
    }

    override fun getUsersByLogins(logins: Collection<String>): List<UserEntity> {
        return userRepository.findAllByLoginIn(logins)
    }

    @AdminAccess
    override fun updateUser(userId: UUID, adminEditUserDto: AdminEditUserDto): AdminUserDto {
        val user = getCurrentUser(userId)
        user.name = adminEditUserDto.name
        user.nickname = adminEditUserDto.nickname
        user.login = adminEditUserDto.login
        user.email = adminEditUserDto.email
        user.avatar = adminEditUserDto.avatar
        user.role = adminEditUserDto.role
        user.gang = adminEditUserDto.gang
        user.setChatBan(adminEditUserDto.chatBan)
        logger.info("Пользователь {} обновлен модератором {}", userMapper.dto(user), getCurrentUser().login)
        return userMapper.adminDto(userRepository.save(user))
    }

    override fun setChatBan(userId: UUID): Boolean {
        val user = getCurrentUser(userId)
        user.setChatBan(!user.chatBan)
        return userRepository.save(user).chatBan
    }

    override fun editUser(user: RegisterUserDto): Status {
        val userEntity = getCurrentUser()
        userEntity.nickname = user.nickname
        // userEntity.setLogin(user.getLogin());
        // TODO добавить через модуль подтверждения почту
        // userEntity.setEmail(user.getEmail());
        userEntity.avatar = user.avatar
        userEntity.password = passwordEncoder.encode(user.password)
        userRepository.save(userEntity)

        val message = messageSource.getMessage("user.updated", null, Locale.of("ru"))
        return Status(true, message)
    }

    override fun deleteUserById(id: UUID) {
        userRepository.deleteById(id)
    }

    @Throws(IOException::class)
    override fun exportMailContacts(): ByteArrayInputStream {
        return exportUsers(userRepository.findAllByReceiveEmailsTrue())
    }

    override fun changeEmailSetting(id: UUID): Boolean {
        val user = userRepository.findById(id).orElseThrow()
        user.receiveEmails = !user.receiveEmails
        return userRepository.save(user)
            .receiveEmails
    }

    @Throws(IOException::class)
    fun exportUsers(users: List<UserEntity>): ByteArrayInputStream {
        logger.info("{} exported {} contacts.", getCurrentUser().login, users.size)
        val workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("users")
        val headerCellStyle: CellStyle = workbook.createCellStyle()
        val row = sheet.createRow(0)
        var cell = row.createCell(0)
        cell.cellStyle = headerCellStyle
        cell = row.createCell(1)
        cell.cellStyle = headerCellStyle
        val userIterator: Iterator<UserEntity> = users.listIterator()
        var i = 0
        while (userIterator.hasNext()) {
            var cellCounter = 0
            val contactEntity = userIterator.next()
            val header = sheet.createRow(i)
            var headerCell = header.createCell(cellCounter++)
            headerCell.cellStyle = headerCellStyle
            headerCell.setCellValue(contactEntity.email)
            headerCell = header.createCell(cellCounter++)
            headerCell.cellStyle = headerCellStyle
            headerCell.setCellValue(contactEntity.login)
            headerCell = header.createCell(cellCounter++)
            headerCell.cellStyle = headerCellStyle
            headerCell.setCellValue(contactEntity.name)
            headerCell = header.createCell(cellCounter)
            headerCell.cellStyle = headerCellStyle
            headerCell.setCellValue(contactEntity.id.toString())
            i++
        }
        sheet.autoSizeColumn(0)
        sheet.autoSizeColumn(1)
        sheet.autoSizeColumn(2)
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return ByteArrayInputStream(outputStream.toByteArray())
    }

    companion object : KLogging() {
        val INITIAL_ROLE = Role.USER
        val UPDATE_ONLINE_DURATION: Duration = Duration.ofMinutes(5)
    }
}
