package net.artux.pdanetwork.service.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.artux.pdanetwork.entity.security.SecurityUser
import net.artux.pdanetwork.repository.user.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val BEARER_PREFIX = "Bearer "

/**
 * Authenticates requests carrying a JWT issued by the Google Play Games
 * sign-in flow. Runs before the Basic Auth filter and simply does nothing
 * when there is no Bearer header, so the existing login/password flow is
 * unaffected.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.substring(BEARER_PREFIX.length)
        val userId = jwtService.parseUserId(token)
        if (userId != null && SecurityContextHolder.getContext().authentication == null) {
            userRepository.findById(userId).ifPresent { user ->
                val authorities = listOf(SimpleGrantedAuthority("ROLE_" + user.role.name))
                val securityUser = SecurityUser(user.login, user.password, authorities, user.id)
                val authentication = UsernamePasswordAuthenticationToken(securityUser, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}
