    package com.mojarras.sys.mojarratores.services

    import com.mojarras.sys.mojarratores.domain.User
    import com.mojarras.sys.mojarratores.domain.toUser
    import com.mojarras.sys.mojarratores.entities.UserEntity
    import com.mojarras.sys.mojarratores.dto.request.UpdateUserRequest
    import com.mojarras.sys.mojarratores.repositories.UserRepository
    import com.mojarras.sys.mojarratores.repositories.toUserEntity
    import org.slf4j.LoggerFactory
    import org.springframework.beans.factory.annotation.Autowired
    import org.springframework.stereotype.Service
    import java.security.MessageDigest
    import java.time.LocalDateTime
    import java.util.UUID

    @Service
    class UserService {

        val logger = LoggerFactory.getLogger(UserService::class.java)

        @Autowired
        lateinit var userRepository: UserRepository


        fun addNewUser(user: User): User? {
            val existingUser = userRepository.findByEmailOrUsername(user.email, user.username)
            if (existingUser != null) {
                return null
            }
            user.password = hashPassword(user.password?: "")

            val userEntity = user.toUserEntity()
            userRepository.save(userEntity)
            val savedUser = userEntity.toUser()
            savedUser.password = "****"
            return savedUser
        }

        fun login (email: String, password: String): User ?{
            val hashedPassword = hashPassword(password)
            val userEntity = userRepository.findUserByPasswordAndEmail(email, hashedPassword)
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

        fun updateUser(token: String, request: UpdateUserRequest): User? {

            val userEntity = userRepository.findByToken(token) ?: return null

            if (!request.email.isNullOrBlank()) {

                val existingUser = userRepository.findByEmail(request.email)

                if (existingUser != null && existingUser.id != userEntity.id) {
                    return null
                }

                userEntity.email = request.email
            }

            if (!request.password.isNullOrBlank()) {
                userEntity.password = hashPassword(request.password!!)
            }

            if (!request.username.isNullOrBlank()) {
                userEntity.username = request.username
            }

            if (!request.firstName.isNullOrBlank()) {
                userEntity.firstName = request.firstName
            }

            if (!request.lastName.isNullOrBlank()) {
                userEntity.lastName = request.lastName
            }

            if (!request.zipCode.isNullOrBlank()) {
                userEntity.zipCode = request.zipCode
            }

            userEntity.updatedAt = LocalDateTime.now()
            userRepository.save(userEntity)

            val user = userEntity.toUser()
            user.password = "****"
            return user
        }

        fun hashPassword(password: String): String {
            val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

    }