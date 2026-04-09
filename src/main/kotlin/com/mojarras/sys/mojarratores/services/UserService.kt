package com.mojarras.sys.mojarratores.services

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.domain.toUser
import com.mojarras.sys.mojarratores.entities.UserEntity
import com.mojarras.sys.mojarratores.repositories.UserRepository
import com.mojarras.sys.mojarratores.repositories.toUserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService {

    val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    lateinit var userRepository: UserRepository


    fun addNewUser(user: User): User? {
        val existingUser = userRepository.findByEmail(user.email)
        if (existingUser != null) {
            return null // email ya registrado
        }

        val userEntity = user.toUserEntity()
        userRepository.save(userEntity)
        val savedUser = userEntity.toUser()
        savedUser.password = "****"
        return savedUser
    }

    fun login (email: String, password: String): User ?{
        val userEntity = userRepository.findUserByPasswordAndEmail(email, password)
            ?: return null

        val token = UUID.randomUUID().toString()
        userRepository.updateTokenById(userEntity.id!!, token)

        val user = userEntity.toUser()
        user.password = "****"
        user.token = token
        return user
    }

    fun logout(token: String): Boolean{
        val userEntity = userRepository.findByToken(token)
            ?: return false

        userRepository.updateTokenById(userEntity.id!!, null)
        return true
    }

    fun getMe(token: String): User?{
        val userEntity = userRepository.findByToken(token)
            ?: return null

        val user = userEntity.toUser()
        user.password = "****"
        return user
    }

    fun updateUser(token: String, updateUser: User): User? {
        val userEntity = userRepository.findByToken(token) ?: return null

        // Verifica que el nuevo email no esté en uso por otro usuario
        val existingUser = userRepository.findByEmail(updateUser.email)
        if (existingUser != null && existingUser.id != userEntity.id) {
            return null
        }

        userEntity.email = updateUser.email
        if (!updateUser.password.isNullOrBlank()) {
            userEntity.password = updateUser.password
        }
        userEntity.updatedAt = java.time.LocalDateTime.now()
        userRepository.save(userEntity)

        val user = userEntity.toUser()
        user.password = "****"
        return user
    }

}