package com.mojarras.sys.mojarratores.services

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.repositories.UserRepository
import com.mojarras.sys.mojarratores.repositories.toUserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    lateinit var userRepository: UserRepository


    fun addNewUser(user: User): User {
        val userEntity = user.toUserEntity()
        userRepository.save(userEntity)
        user.password = "****"
        return user
    }


}