package com.mojarras.sys.mojarratores.user.controllers

import com.mojarras.sys.mojarratores.dto.response.LogoutResponse
import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.domain.toUser
import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.dto.request.UpdateUserRequest
import com.mojarras.sys.mojarratores.dto.request.LoginRequest
import com.mojarras.sys.mojarratores.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controlador para endpoints del usuario
 * */
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    lateinit var userService: UserService

    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    /**
     * Endpoint para consultar el usuario autenticado
     * */
    @GetMapping("/me")
    fun retrieveUser(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<User> {

        val user = userService.getMe(token)
            ?: return ResponseEntity.status(401).build()

        logger.info("User found in database: $user")
        return ResponseEntity.ok(user)
    }


    /**
     * Endpoint para registrar un usuario
     * */
    @PostMapping("/register")
    fun register(
        @RequestBody createUserRequest: CreateUserRequest
    ): ResponseEntity<Any> {


        if (createUserRequest.password.isBlank()) {
            return ResponseEntity.status(400)
                .body(mapOf("error" to "Password cannot be empty"))
        }
        val newUser = createUserRequest.toUser()

        val addedUser = userService.addNewUser(newUser)
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

    /**
     * Endpoint para iniciar sesión
     * */
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<User> {

        val user = userService.login(loginRequest.email, loginRequest.password)
            ?: run{
                logger.error("Login failed for: $loginRequest ")
                return ResponseEntity.status(401).build()
            }
        logger.info("Login sccessful for: $loginRequest")
        return ResponseEntity.ok(user)
    }


    /**
     * Endpoint que simula cerrar sesion del usuario
     * */
    @PostMapping("/logout")
    @ResponseBody
    fun logout(
        @RequestHeader ("Authorization") token: String
    ): ResponseEntity<LogoutResponse> {

        val exit = userService.logout(token)

        if (!exit) {
            logger.error("Logout failed, token $token not found")
            return ResponseEntity.status(401).build()
        }
        logger.info("Logout successful for $token")
        val response = LogoutResponse(
            userId = token,
            logoutDateTime = LocalDateTime.now().toString()
        )
        return ResponseEntity.ok(response)
    }

    /**
     *  Endpoint que simula la actualización de  un usuario
     * */
    @PutMapping
    fun updateUser(
        @RequestHeader("Authorization") token: String,
        @RequestBody updateUserRequest: UpdateUserRequest
    ): ResponseEntity<Any>{

        if (updateUserRequest.password != null && updateUserRequest.password.isBlank()) {
            return ResponseEntity.status(400)
                .body(mapOf("error" to "Password cannot be empty"))
        }

        val updatedUser = userService.updateUser(token, updateUserRequest)
            ?: return ResponseEntity.status(409).build()

        logger.info("Info to update: $updateUserRequest")

        return ResponseEntity.ok(updatedUser)
    }

}