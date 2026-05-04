package com.mojarras.sys.mojarratores.user.dto.request

data class UpdateUserRequest (
    val email: String?,
    val password: String?,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val zipCode: String?
)