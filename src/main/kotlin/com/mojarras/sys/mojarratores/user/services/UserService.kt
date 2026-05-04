package com.mojarras.sys.mojarratores.user.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.ConflictException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.exception.UnauthorizedException
import com.mojarras.sys.mojarratores.security.JwtUtil
import com.mojarras.sys.mojarratores.user.domain.User
import com.mojarras.sys.mojarratores.user.dto.request.UpdateUserRequest
import com.mojarras.sys.mojarratores.user.dto.response.AuthResponse
import com.mojarras.sys.mojarratores.user.mapper.toUser
import com.mojarras.sys.mojarratores.user.mapper.toUserEntity
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime

@Service
class UserService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun addNewUser(user: User): User {

        if (userRepository.existsByEmail(user.email)) {
            throw ConflictException("Email already in use")
        }

        if (userRepository.existsByUsername(user.username)) {
            throw ConflictException("Username already in use")
        }

        val encodedPassword = requireNotNull(passwordEncoder.encode(user.password)) {
            "Password encoding failed"
        }
        val userEntity = user.toUserEntity(encodedPassword)
        val savedUser = userRepository.save(userEntity)

        logger.info("User added: ${savedUser.email}")

        return savedUser.toUser()
    }

    fun login (email: String, password: String): AuthResponse {

        val entity = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")


        if (!passwordEncoder.matches(password, entity.passwordHash)) {
            throw UnauthorizedException("Invalid credentials")
        }

        val token = jwtUtil.generateToken(entity.email)

        logger.info("Login success: ${entity.email}")

        return AuthResponse(
            token,
            Instant.now().plusSeconds(jwtUtil.expiration).toString()
        )
    }

    /*
    fun logout(token: String): Boolean{
        val userEntity = userRepository.findByToken(token)
            ?: return false

        userRepository.updateTokenById(userEntity.id!!, null)
        return true
    }
     */

    fun getMe(email: String): User{
        return userRepository.findByEmail(email)
            ?.toUser()
            ?: throw NotFoundException("User not found")
    }

    fun updateUser(email: String, request: UpdateUserRequest): User {

        val userEntity = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        request.username?.let { userEntity.username = it }
        request.firstName?.let { userEntity.firstName = it }
        request.lastName?.let { userEntity.lastName = it }
        request.zipCode?.let { userEntity.zipCode = it }

        request.password?.let {
            if (it.length < 6) throw BadRequestException("Password too short")
            userEntity.passwordHash = requireNotNull(passwordEncoder.encode(it)) {
                "Password encoding failed"
            }
        }

        userEntity.updatedAt = LocalDateTime.now()

        val updatedUser =  userRepository.save(userEntity).toUser()

        logger.info("User updated: ${updatedUser.email}")

        return updatedUser
    }
}