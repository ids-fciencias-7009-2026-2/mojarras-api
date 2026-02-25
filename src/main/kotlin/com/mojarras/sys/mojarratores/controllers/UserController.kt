package com.mojarras.sys.mojarratores.controllers

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.domain.toUser
import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
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

}