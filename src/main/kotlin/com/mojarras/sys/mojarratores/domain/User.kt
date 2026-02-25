package com.mojarras.sys.mojarratores.domain

data class User(
    val idUser: String,
    var username: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String? = null,
    var zipCode: String? = null
)
