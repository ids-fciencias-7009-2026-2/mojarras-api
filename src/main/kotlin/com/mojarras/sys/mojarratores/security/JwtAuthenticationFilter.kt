package com.mojarras.sys.mojarratores.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)

            try {
                val username = jwtUtil.extractUsername(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)

                    if (jwtUtil.validateToken(token, userDetails.username)) {
                        val auth = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )

                        auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = auth
                    }
                }

            } catch (ex: ExpiredJwtException) {
                logger.warn("JWT Expirado: ${ex.message}")
                request.setAttribute("jwt_error", "El token ha expirado.")
            } catch (ex: SignatureException) {
                logger.error("Firma de JWT inválida: ${ex.message}")
                request.setAttribute("jwt_error", "La firma del token es inválida.")
            } catch (ex: MalformedJwtException) {
                logger.warn("JWT Mal formado: ${ex.message}")
                request.setAttribute("jwt_error", "El formato del token no es válido.")
            } catch (ex: Exception) {
                logger.error("Error no controlado en JWT: ${ex.message}")
                request.setAttribute("jwt_error", "Error de autenticación.")
            }
        }

        chain.doFilter(request, response)
    }
}