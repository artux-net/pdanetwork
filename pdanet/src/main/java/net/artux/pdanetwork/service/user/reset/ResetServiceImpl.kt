package net.artux.pdanetwork.service.user.reset

import jakarta.mail.MessagingException
import net.artux.pdanetwork.controller.web.user.PasswordController
import net.artux.pdanetwork.entity.communication.ConversationEntity
import net.artux.pdanetwork.entity.mappers.StoryMapper
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.dto.StoryData
import net.artux.pdanetwork.repository.comminication.ConversationRepository
import net.artux.pdanetwork.repository.comminication.MessageRepository
import net.artux.pdanetwork.repository.feed.ArticleLikeRepository
import net.artux.pdanetwork.repository.feed.CommentRepository
import net.artux.pdanetwork.repository.feed.PostRepository
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.email.EmailService
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.service.util.ValuesService
import net.artux.pdanetwork.utils.RandomString
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.Timer
import java.util.TimerTask

@Service
@Suppress("LongParameterList")
open class ResetServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
    private val emailService: EmailService,
    private val userRepository: UserRepository,
    private val storyMapper: StoryMapper,
    private val valuesService: ValuesService,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) : ResetService {

    private val logger = LoggerFactory.getLogger(ResetServiceImpl::class.java)
    private val randomString = RandomString()
    private val timer = Timer()
    private val requests = HashMap<String, String>()

    override fun sendResetPasswordLetter(email: String): Status {
        val optionalUser = userRepository.findByEmail(email)
        if (optionalUser.isEmpty) {
            return Status(false, "Такого пользователя не существует, либо письмо уже отправлено")
        }

        val userEntity = optionalUser.get()
        return if (!requests.containsValue(userEntity.email)) {
            val token = randomString.nextString()
            logger.info(
                "Ссылка для сброса пароля {} для пользователя: {}",
                valuesService.address + PasswordController.RESET_PASSWORD_URL + "?t=" + token,
                userEntity.login
            )
            addCurrent(token, userEntity)

            try {
                emailService.askForPassword(userEntity, token)
                Status(true, "Мы отправили письмо с паролем на Вашу почту")
            } catch (e: MessagingException) {
                Status(false, e.message)
            }
        } else {
            Status(false, "Такого пользователя не существует, либо письмо уже отправлено")
        }
    }

    private fun addCurrent(token: String, user: UserEntity) {
        requests[token] = user.email
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    requests.remove(token)
                }
            },
            WAIT_LIST_DURATION.toMillis()
        )
    }

    override fun changePassword(token: String, password: String): Status {
        var userEntity = try {
            val email = requests[token] ?: throw NoSuchElementException()

            userRepository.findByEmail(email).orElseThrow()
        } catch (ignored: NoSuchElementException) {
            return Status(false, "Токен или пользователь не найден")
        }

        logger.info("Изменение пароля для пользователя: {}", userEntity.login)
        logger.info("Хэш старого пароля: {}", userEntity.password)
        userEntity.password = passwordEncoder.encode(password)
        userEntity = userRepository.save(userEntity)
        requests.remove(token)
        logger.info("Хэш нового пароля: {}", userEntity.password)

        return Status(true, "Пароль успешно изменен")
    }

    @Transactional
    override fun resetData(): StoryData {
        logger.info("Сброс истории прохождения")
        val userEntity = userService.getCurrentUser()
        userEntity.reset()
        userEntity.storyStates.clear()
        return storyMapper.storyData(userRepository.saveAndFlush(userEntity))
    }

    override fun getTokens(): Collection<String> {
        return requests.keys
    }

    @Transactional
    override fun deleteAccount(): Boolean {
        logger.info("Удаление аккаунта")
        val user = userService.getCurrentUser()
        user.reset()
        userRepository.save(user)
        messageRepository.deleteAllByAuthor(user)
        articleLikeRepository.deleteAllByUser(user)
        commentRepository.deleteAllByAuthor(user)
        postRepository.deleteAllByAuthor(user)
        conversationRepository.deleteAllByMembersContainsAndTypeEquals(user, ConversationEntity.Type.PRIVATE)
        userRepository.deleteById(user.id)
        return true
    }

    companion object {
        val WAIT_LIST_DURATION: Duration = Duration.ofMinutes(10)
    }
}
