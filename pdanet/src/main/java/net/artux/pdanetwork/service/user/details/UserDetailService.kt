package net.artux.pdanetwork.service.user.details

import jakarta.annotation.PostConstruct
import net.artux.pdanetwork.entity.security.SecurityUser
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.models.user.enums.Role
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.utills.RandomString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
open class UserDetailService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val randomString: RandomString,
    private val environment: Environment,
) : UserDetailsService {
    private val logger: Logger = LoggerFactory.getLogger(UserDetailService::class.java)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        if (username.isBlank()) throw UsernameNotFoundException("Access denied.")
        val userOptional = if (username.contains("@")) {
            userRepository.getByEmail(username)
        } else {
            userRepository.getByLogin(username)
        }

        if (userOptional.isPresent) {
            val simpleUser = userOptional.get()
            val userDetails = User.builder()
                .username(simpleUser.login)
                .password(simpleUser.password)
                .authorities("ROLE_" + simpleUser.role.name)
                .build()
            return SecurityUser(simpleUser.id, userDetails)
        } else {
            logger.error("User with login '$username' not found.")
            throw UsernameNotFoundException("User not found")
        }
    }

    @Suppress("UnusedPrivateMember")
    @PostConstruct
    private fun registerFirstUsers() {
        val email = environment.getProperty("administrator.email")
        val nickname = environment.getProperty("administrator.nickname")

        val password = randomString.nextString()

        val registerUserDto = RegisterUserDto.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .avatar("1")
            .build()

        if (userRepository.count() < 1) {
            userRepository.save(
                UserEntity(
                    registerUserDto,
                    passwordEncoder,
                    Role.ADMIN
                )
            )
            logger.info(
                """
                    There are no users, a user with a admin role created. 
                     Email: {} 
                     Password: {}                  
                """.trimIndent(),
                email,
                password
            )
        }
    }
}
