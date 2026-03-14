package com.mojarras.sys.mojarratores.domain

data class User(
    val id: Long? = null,
    var email: String,
    var password: String? = null,
    var username: String,
    var firstName: String,
    var lastName: String,
    var zipCode: String? = null,
    var token: String? = null
)
