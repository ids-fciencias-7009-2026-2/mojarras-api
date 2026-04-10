package com.mojarras.sys.mojarratores.controllers

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.domain.toUser
import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.dto.request.LoginRequest
import com.mojarras.sys.mojarratores.dto.request.UpdateUserRequest
import com.mojarras.sys.mojarratores.dto.response.LogoutResponse
import com.mojarras.sys.mojarratores.services.UserService
import java.security.MessageDigest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/** Controlador para endpoints del usuario */
@CrossOrigin(origins = ["*"])
@Controller
@RequestMapping("/users")
class UserController {

    @Autowired lateinit var userService: UserService

    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    /** Endpoint para consultar el usuario autenticado */
    @GetMapping("/me")
    fun retrieveUser(@RequestHeader("Authorization") token: String): ResponseEntity<User> {

        val user = userService.getMe(token) ?: return ResponseEntity.status(401).build()

        logger.info("User found in database: $user")
        return ResponseEntity.ok(user)
    }

    /** Endpoint para registrar un usuario */
    @PostMapping("/register")
    fun register(@RequestBody createUserRequest: CreateUserRequest): ResponseEntity<Any> {

        val newUser = createUserRequest.toUser()

        val password = hashPassword(createUserRequest.password)
        newUser.password = password

        val addedUser =
                userService.addNewUser(newUser)
                        ?: return ResponseEntity.status(409)
                                .body(
                                        mapOf(
                                                "error" to
                                                        "User with that email or username already exists"
                                        )
                                )

        logger.info("User added: $addedUser")

        return ResponseEntity.ok(addedUser)
    }

    /** Endpoint para iniciar sesión */
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<User> {

        val hashedPassword = hashPassword(loginRequest.password)
        val user =
                userService.login(loginRequest.email, hashedPassword)
                        ?: run {
                            logger.error("Login failed for: $loginRequest ")
                            return ResponseEntity.status(401).build()
                        }
        logger.info("Login sccessful for: $loginRequest")
        return ResponseEntity.ok(user)
    }

    /** Endpoint que simula cerrar sesion del usuario */
    @PostMapping("/logout")
    @ResponseBody
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<LogoutResponse> {

        val exit = userService.logout(token)

        if (!exit) {
            logger.error("Logout failed, token $token not found")
            return ResponseEntity.status(401).build()
        }
        logger.info("Logout successful for $token")
        val response =
                LogoutResponse(
                        userId = token,
                        logoutDateTime = java.time.LocalDateTime.now().toString()
                )
        return ResponseEntity.ok(response)
    }

    /** Endpoint que simula la actualización de un usuario */
    @PutMapping
    fun updateInfoUser(@RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<Any> {
        val dummyUser =
                User(
                        1,
                        "mojarrita21",
                        "Mojarra",
                        "Tilapia",
                        "mojarra@frita.com",
                        "mojarra123",
                        "9999"
                )

        logger.info($$"User found: $user")
        logger.info("Info to update: $updateUserRequest")

        dummyUser.email = updateUserRequest.email
        dummyUser.password = updateUserRequest.password

        return ResponseEntity.ok(dummyUser)
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
