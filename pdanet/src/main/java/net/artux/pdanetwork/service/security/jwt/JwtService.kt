package net.artux.pdanetwork.service.security.jwt

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

/**
 * Issues and verifies the bearer tokens used by players who authenticate via
 * Google Play Games. Basic Auth (login/password) stays untouched for the
 * existing manual registration flow; this is an additive auth path.
 */
@Service
class JwtService(
    @Value("\${security.jwt.secret}") secret: String,
    @Value("\${security.jwt.expiration-days:180}") private val expirationDays: Long,
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))

    fun generateToken(userId: UUID): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(Duration.ofDays(expirationDays))))
            .signWith(key)
            .compact()
    }

    fun parseUserId(token: String): UUID? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            UUID.fromString(claims.subject)
        } catch (e: JwtException) {
            logger.debug("Rejected JWT: {}", e.message)
            null
        } catch (e: IllegalArgumentException) {
            logger.debug("Rejected JWT: {}", e.message)
            null
        }
    }

    companion object : KLogging()
}
