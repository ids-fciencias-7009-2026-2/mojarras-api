package com.mojarras.sys.mojarratores.user.dto.response

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val zipCode: String
)