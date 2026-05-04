package com.mojarras.sys.mojarratores.user.domain

data class User(
    val id: Long? = null,
    val email: String,
    val password: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val zipCode: String
)
