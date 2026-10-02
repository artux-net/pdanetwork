package net.artux.pdanetwork.service.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KLogging
import net.artux.pdanetwork.entity.mappers.UserMapper
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.exception.GooglePlayGamesAuthException
import net.artux.pdanetwork.models.user.dto.AuthTokenDto
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.security.jwt.JwtService
import net.artux.pdanetwork.utils.RandomString
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.Instant

/**
 * Authenticates players by their Google Play Games account. The Android
 * client obtains a one-time server auth code via the Play Games Services v2
 * sign-in SDK (`GamesSignInClient.requestServerSideAccess`); this service
 * exchanges it for an access token, resolves the stable Play Games player
 * id, and either logs the matching account in or creates one on the fly.
 *
 * This is additive: it issues a JWT handled by [net.artux.pdanetwork.service.security.jwt.JwtAuthenticationFilter]
 * and never touches the login/password Basic Auth flow used elsewhere.
 */
@Service
open class GooglePlayGamesAuthService(
    @Value("\${google.play-games.client-id}") private val clientId: String,
    @Value("\${google.play-games.client-secret}") private val clientSecret: String,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val userMapper: UserMapper,
) {

    private val randomString = RandomString(32)
    private val restTemplate = RestTemplate()

    @Transactional
    open fun authenticate(serverAuthCode: String): AuthTokenDto {
        val accessToken = exchangeAuthCode(serverAuthCode)
        val player = fetchPlayer(accessToken)

        val user = userRepository.findByGooglePlayPlayerId(player.playerId)
            .orElseGet { createUser(player) }

        user.lastLoginAt = Instant.now()
        userRepository.save(user)

        return AuthTokenDto(jwtService.generateToken(user.id), userMapper.dto(user))
    }

    private fun createUser(player: PlayGamesPlayer): UserEntity {
        logger.info("Создан аккаунт на лету для игрока Google Play Games {}", player.playerId)
        return userRepository.save(
            UserEntity(
                player.playerId,
                sanitizeNickname(player.displayName),
                DEFAULT_AVATAR,
                passwordEncoder,
                randomString.nextString()
            )
        )
    }

    private fun sanitizeNickname(displayName: String?): String {
        val sanitized = (displayName ?: "")
            .filter { it.isLetter() || it == '\'' || it == ' ' }
            .trim()
            .take(MAX_NICKNAME_LENGTH)
        return if (sanitized.length >= MIN_NICKNAME_LENGTH) sanitized else DEFAULT_NICKNAME
    }

    private fun exchangeAuthCode(serverAuthCode: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("code", serverAuthCode)
        body.add("client_id", clientId)
        body.add("client_secret", clientSecret)
        body.add("grant_type", "authorization_code")

        val response = try {
            restTemplate.postForObject(GOOGLE_TOKEN_URL, HttpEntity(body, headers), GoogleTokenResponse::class.java)
        } catch (e: RestClientException) {
            throw GooglePlayGamesAuthException("Не удалось подтвердить код авторизации Google Play Games", e)
        }

        return response?.accessToken
            ?: throw GooglePlayGamesAuthException("Google не вернул access token")
    }

    private fun fetchPlayer(accessToken: String): PlayGamesPlayer {
        val headers = HttpHeaders()
        headers.setBearerAuth(accessToken)

        val response = try {
            restTemplate.exchange(
                PLAY_GAMES_PLAYER_URL,
                HttpMethod.GET,
                HttpEntity<Void>(headers),
                PlayGamesPlayerResponse::class.java
            ).body
        } catch (e: RestClientException) {
            throw GooglePlayGamesAuthException("Не удалось получить данные игрока из Play Games Services", e)
        }

        val playerId = response?.playerId
            ?: throw GooglePlayGamesAuthException("Play Games не вернул playerId")

        return PlayGamesPlayer(playerId, response.displayName)
    }

    private data class PlayGamesPlayer(val playerId: String, val displayName: String?)

    // Plain bean-style classes (no-arg constructor + setters) rather than Kotlin
    // data classes: this codebase doesn't depend on jackson-module-kotlin, so
    // Jackson needs the usual property setters to deserialize these.
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class GoogleTokenResponse {
        @JsonProperty("access_token")
        var accessToken: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class PlayGamesPlayerResponse {
        var playerId: String? = null
        var displayName: String? = null
    }

    companion object : KLogging() {
        private const val GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token"
        private const val PLAY_GAMES_PLAYER_URL = "https://games.googleapis.com/games/v1/players/me"
        private const val DEFAULT_AVATAR = "1"
        private const val DEFAULT_NICKNAME = "Rookie"
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 16
    }
}
