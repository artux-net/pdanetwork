package net.artux.pdanetwork.utils

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KLogging
import net.artux.pdanetwork.entity.mappers.ErrorMapper
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.IOException

@RestControllerAdvice
@Suppress("UnusedParameter")
class GlobalExceptionHandler(
    private val errorMapper: ErrorMapper,
    private val objectMapper: ObjectMapper,
) : ResponseEntityExceptionHandler(), AuthenticationEntryPoint {

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception?, request: WebRequest?): ResponseEntity<Any> {
        logger.error("Got error while ", ex)
        return ResponseEntity(errorMapper.of(ex, "Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun handleServletRequestBindingException(
        ex: ServletRequestBindingException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(errorMapper.of(ex, "Bind Exception"), HttpStatus.BAD_REQUEST)
    }

    override fun handleTypeMismatch(
        ex: TypeMismatchException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(errorMapper.of(ex, "TypeMismatchException Exception"), HttpStatus.BAD_REQUEST)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(errorMapper.of(ex, "MethodArgumentNotValidException Exception"), HttpStatus.BAD_REQUEST)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.println(
            objectMapper.writeValueAsString(
                errorMapper.of("Токен аунтефикации истек или он невалидный, выполните вход.", "Authorization Error")
            )
        )
    }

    companion object : KLogging()
}
