package com.mojarras.sys.mojarratores.dto.request

data class CreateUserRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val zipCode: String,
    val password: String
)
