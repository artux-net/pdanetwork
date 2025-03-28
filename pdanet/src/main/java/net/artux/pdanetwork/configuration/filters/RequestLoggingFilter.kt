package net.artux.pdanetwork.configuration.filters

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import net.artux.pdanetwork.entity.security.SecurityUser
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.io.IOException

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
open class RequestLoggingFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            val securityUser = SecurityContextHolder.getContext().authentication.principal
            if (request is HttpServletRequest) {
                MDC.put("method", request.method)
                MDC.put("path", request.requestURI)
                MDC.put("remoteAddr", request.getRemoteAddr())
                MDC.put("userAgent", request.getHeader("User-Agent"))
                MDC.put("locale", request.getHeader("Accept-Language"))
                MDC.put("pdaVersion", request.getHeader("X-PDA-Version"))
            }

            if (securityUser is SecurityUser) {
                MDC.put("userId", securityUser.id.toString())
                MDC.put("username", securityUser.username)
            } else {
                MDC.put("username", securityUser.toString())
            }

            chain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
