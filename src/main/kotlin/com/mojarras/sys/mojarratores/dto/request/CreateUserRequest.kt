package com.mojarras.sys.mojarratores.dto.request

data class CreateUserRequest(
    val email: String,
    val password: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val zipCode: String
)
