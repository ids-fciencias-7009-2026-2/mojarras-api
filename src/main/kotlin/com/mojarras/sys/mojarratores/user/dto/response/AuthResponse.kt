package com.mojarras.sys.mojarratores.user.dto.response

data class AuthResponse(
    val token: String,
    val expiresAt: String
)
