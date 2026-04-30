package com.mojarras.sys.mojarratores.user.dto.request

import jakarta.validation.constraints.*

data class CreateUserRequest(

    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    @field:Size(min = 6)
    val password: String,

    @field:NotBlank
    val username: String,

    @field:NotBlank
    val firstName: String,

    @field:NotBlank
    val lastName: String,

    @field:NotBlank
    val zipCode: String
)
