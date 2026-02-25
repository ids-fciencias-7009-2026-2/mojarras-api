package com.mojarras.sys.mojarratores.controllers

import com.mojarras.sys.mojarratores.dto.response.LogoutResponse
import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.domain.toUser
import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.dto.request.UpdateUserRequest
import com.mojarras.sys.mojarratores.dto.request.LoginRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/usuarios")
class UserController {

    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)
    @GetMapping("/me")
    fun retrieveUser(): ResponseEntity<User> {

        val dummyUser = User(
            "1",
            "mojarra123",
            "Mojarra Tilapia",
            "mojarra@frita.com",
            "mojarra123",
            "91900"
        )

        logger.info("User found in database: $dummyUser")

        return ResponseEntity.ok(dummyUser)
    }

    /**
     * Endpoint que simula registrar un usuario
     * */
    @PostMapping("/register")
    fun register(
        @RequestBody createUserRequest: CreateUserRequest
    ): ResponseEntity<User> {

        val newUser = createUserRequest.toUser()

        logger.info("Usuario para agregar: $newUser")

        return ResponseEntity.ok(newUser)
    }


    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {

        val dummyUser = User(
            "1",
            "mojarra123",
            "Mojarra",
            "Tilapia",
            "mojarra@frita.com",
            "mojarra123",
            "91900"
        )

        logger.info("try make login with: $loginRequest")

        return if (dummyUser.password == loginRequest.password) {
            logger.info("Login successful")
            ResponseEntity.ok(Any())

        } else {
            logger.error("Login failed")
            ResponseEntity.status(401).build()
        }
    }


    /** 
    * Endpoint que simula cerrar sesion del usuario
    * */
    @PostMapping("/logout")
    @ResponseBody
    fun logout(
        @RequestBody userId: String 
    ): ResponseEntity<LogoutResponse> {
        
        logger.info("Cerrando sesión para el ID: $userId")

        val response = LogoutResponse(
            userId = userId,
            logoutDateTime = java.time.LocalDateTime.now().toString()
        )

        return ResponseEntity.ok(response)
    }

    /**
     *  Endpoint que simula la actualización de  un usuario
     * */
    @PutMapping
    fun updateInfoUser(
        @RequestBody updateUserRequest: UpdateUserRequest
    ): ResponseEntity<Any>{
        val user = User(
            idUser = "1",
            username = "mojarrita21",
            firstName = "Mojarra",
            lastName = "Tilapia",
            email = "mojarra@frita.com",
            password = "mojarra123",
            zipCode = "9999"
        )

        logger.info("User found: \$user")
        logger.info("Info to update: $updateUserRequest")

        user.email = updateUserRequest.email
        user.password = updateUserRequest.password

        return ResponseEntity.ok(user)
    }

}
