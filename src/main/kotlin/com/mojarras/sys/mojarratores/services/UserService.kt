package com.mojarras.sys.mojarratores.services

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.domain.toUser
import com.mojarras.sys.mojarratores.repositories.UserRepository
import com.mojarras.sys.mojarratores.repositories.toUserEntity
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired lateinit var userRepository: UserRepository

    fun addNewUser(user: User): User? {
        val existingUser = userRepository.findByEmailOrUsername(user.email, user.username)
        if (existingUser != null) {
            logger.error(
                    "User with email ${user.email} or username ${user.username} already exists"
            )
            return null
        }

        val userEntity = user.toUserEntity()
        userRepository.save(userEntity)
        user.password = "****"
        return user
    }

    fun login(email: String, password: String): User? {
        val userEntity = userRepository.findUserByPasswordAndEmail(email, password) ?: return null

        val token = UUID.randomUUID().toString()
        userRepository.updateTokenById(userEntity.id!!, token)

        val user = userEntity.toUser()
        user.password = "****"
        user.token = token
        return user
    }

    fun logout(token: String): Boolean {
        val userEntity = userRepository.findByToken(token) ?: return false

        userRepository.updateTokenById(userEntity.id!!, null)
        return true
    }

    fun getMe(token: String): User? {
        val userEntity = userRepository.findByToken(token) ?: return null

        val user = userEntity.toUser()
        user.password = "****"
        return user
    }
}
