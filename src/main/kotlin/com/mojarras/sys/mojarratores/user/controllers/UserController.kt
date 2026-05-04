package com.mojarras.sys.mojarratores.user.controllers

import com.mojarras.sys.mojarratores.user.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.user.dto.request.UpdateUserRequest
import com.mojarras.sys.mojarratores.user.dto.request.LoginRequest
import com.mojarras.sys.mojarratores.user.dto.response.AuthResponse
import com.mojarras.sys.mojarratores.user.dto.response.UserResponse
import com.mojarras.sys.mojarratores.user.mapper.toUser
import com.mojarras.sys.mojarratores.user.mapper.toUserResponse
import com.mojarras.sys.mojarratores.user.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/users")
class UserController (
    private val userService: UserService
) {

    @GetMapping("/me")
    fun retrieveUser(
        authentication: Authentication
    ): ResponseEntity<UserResponse> {

        val user = userService.getMe(authentication.name)

        return ResponseEntity.ok(user.toUserResponse())
    }

    @PostMapping()
    fun register(
        @Valid @RequestBody createUserRequest: CreateUserRequest
    ): ResponseEntity<UserResponse> {

        val newUser = createUserRequest.toUser()

        val addedUser = userService.addNewUser(newUser)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(addedUser.toUserResponse())
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<AuthResponse> {

        val authResponse = userService.login(loginRequest.email, loginRequest.password)
        return ResponseEntity.ok(authResponse)
    }

    /*
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
     */

    @PutMapping
    fun updateUser(
        authentication: Authentication,
        @Valid @RequestBody updateUserRequest: UpdateUserRequest
    ): ResponseEntity<UserResponse>{

        val updatedUser = userService.updateUser(authentication.name, updateUserRequest)

        return ResponseEntity.ok(updatedUser.toUserResponse())
    }
}