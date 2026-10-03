package net.artux.pdanetwork.service.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

// No Spring context needed: JwtService has no external dependencies.
class JwtServiceTest {

    private val secret = "a".repeat(32)
    private val jwtService = JwtService(secret, EXPIRATION_DAYS)

    @Test
    fun `generateToken then parseUserId round-trips the same id`() {
        val userId = UUID.randomUUID()

        val token = jwtService.generateToken(userId)

        assertEquals(userId, jwtService.parseUserId(token))
    }

    @Test
    fun `parseUserId rejects a token signed with a different key`() {
        val token = Jwts.builder()
            .subject(UUID.randomUUID().toString())
            .signWith(Keys.hmacShaKeyFor("b".repeat(32).toByteArray()))
            .compact()

        assertNull(jwtService.parseUserId(token))
    }

    @Test
    fun `parseUserId rejects a token with a non-UUID subject`() {
        val token = Jwts.builder()
            .subject("not-a-uuid")
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()

        assertNull(jwtService.parseUserId(token))
    }

    @Test
    fun `parseUserId rejects an expired token`() {
        val now = Instant.now()
        val token = Jwts.builder()
            .subject(UUID.randomUUID().toString())
            .issuedAt(Date.from(now.minus(Duration.ofDays(2))))
            .expiration(Date.from(now.minus(Duration.ofDays(1))))
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()

        assertNull(jwtService.parseUserId(token))
    }

    @Test
    fun `parseUserId rejects garbage input`() {
        assertNull(jwtService.parseUserId("not.a.jwt"))
    }

    @Test
    fun `rejects a secret shorter than the HS256 minimum`() {
        assertFailsWith<IllegalArgumentException> {
            JwtService("too-short", EXPIRATION_DAYS)
        }
    }

    companion object {
        private const val EXPIRATION_DAYS = 180L
    }
}
