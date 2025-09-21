package net.artux.pdanetwork.service.user

import jakarta.annotation.PostConstruct
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.repository.user.UserRepository
import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.stream.Stream

@Component
class UserValidator(
    private val userRepository: UserRepository,
) {

    private var blockedNicknames: String? = null

    @PostConstruct
    @Throws(IOException::class)
    @Suppress("UnusedPrivateMember")
    private fun initBlockedNicknames() {
        val resource: Resource = ClassPathResource("/config/forbidden_nicks.txt")
        val nicknamesFile = IOUtils.toString(resource.inputStream, StandardCharsets.UTF_8)
        blockedNicknames = nicknamesFile.lowercase()
    }

    fun checkUser(user: RegisterUserDto): Status {
        return Stream.of(
            checkNickname(user.nickname),
            checkPassword(user.password),
            checkEmail(user.email)
        )
            .filter { status: Status -> !status.isSuccess }
            .findFirst()
            .orElse(Status(true, "Логин и почта свободны."))
    }

    private fun checkNickname(nickname: String): Status {
        return if (!StringUtils.hasText(nickname)) {
            Status(false, "Прозвище не может быть пустым.")
        } else {
            val defectSymbols = checkStringSymbolsByRegexp(nickname, NAME_VALIDATION_REGEX)
            if (!defectSymbols.isEmpty()) {
                Status(
                    false,
                    "Прозвище содержит запрещённые символы: " + java.lang.String.join(", ", defectSymbols)
                )
            } else if (nickname.length < NAME_MIN_LENGTH || nickname.length > NAME_MAX_LENGTH) {
                Status(false, "Прозвище должно содержать не менее 2 и не более 16 символов.")
            } else if (blockedNicknames!!.contains(nickname.lowercase())) {
                Status(
                    false,
                    "Прозвище должно быть уникальным."
                )
            } else {
                Status(true)
            }
        }
    }

    private fun checkPassword(password: String): Status {
        return if (!StringUtils.hasText(password)) {
            Status(false, "Пароль не может быть пустым.")
        } else {
            val defectSymbols = checkStringSymbolsByRegexp(password, PASSWORD_VALIDATION_REGEX)
            if (!defectSymbols.isEmpty()) {
                Status(
                    false,
                    "Пароль содержит запрещённые символы: " + java.lang.String.join(", ", defectSymbols)
                )
            } else if (password.length < PASSWORD_MIN_LENGTH || password.length > PASSWORD_MAX_LENGTH) {
                Status(false, "Пароль должен содержать не менее 8 и не более 24 символов.")
            } else {
                Status(true)
            }
        }
    }

    private fun checkEmail(email: String): Status {
        return if (!StringUtils.hasText(email)) {
            Status(false, "Почта не может быть пустой.")
        } else if (!email.matches(EMAIL_VALIDATION_REGEX.toRegex())) {
            Status(false, "Почта не существует.")
       /* } else if (FORBIDDEN_MAIL_DOMAINS.contains(email.substringAfter("@"))) {
            Status(false, "Временная почта запрещена.")*/
        } else if (userRepository.findByEmail(email).isPresent) {
            Status(false, "Пользователь с таким e-mail уже существует.")
        } else {
            Status(true)
        }
    }

    private fun checkStringSymbolsByRegexp(str: String, regexp: String): Collection<String> {
        if (!StringUtils.hasText(str)) {
            return emptyList()
        }
        val result: MutableCollection<String> = ArrayList()
        for (chr in str.toCharArray()) {
            val chrOfStr = chr.toString()
            if (!chrOfStr.matches(regexp.toRegex())) {
                result.add(chrOfStr)
            }
        }
        return result
    }

    companion object {
        private const val EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
        private const val NAME_VALIDATION_REGEX = "^[A-Za-z\u0400-\u052F' ]*$"
        private const val PASSWORD_VALIDATION_REGEX = "^[A-Za-z\\d!@#$%^&*()_+№\";:?><\\[\\]{}]*$"

        private const val NAME_MAX_LENGTH = 32
        private const val NAME_MIN_LENGTH = 2
        private const val PASSWORD_MIN_LENGTH = 8
        private const val PASSWORD_MAX_LENGTH = 24
    }
}
