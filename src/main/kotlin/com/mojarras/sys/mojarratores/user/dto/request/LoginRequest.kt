package com.mojarras.sys.mojarratores.user.dto.request

import jakarta.validation.constraints.*

data class LoginRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)