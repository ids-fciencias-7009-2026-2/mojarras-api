package com.mojarras.sys.mojarratores.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.time.LocalDateTime

@Component
class JwtAuthenticationEntryPoint(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val specificError = request.getAttribute("jwt_error") as? String
        val message = specificError ?: "Acceso denegado: Token no proporcionado o inválido."

        val body = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpServletResponse.SC_FORBIDDEN,
            "error" to "Forbidden",
            "message" to message,
            "path" to request.servletPath
        )

        objectMapper.writeValue(response.outputStream, body)
    }
}