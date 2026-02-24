package com.mojarras.sys.mojarratores.domain

data class User(
    val id: String,
    var username: String,
    var name: String,
    var email: String,
    var password: String? = null,
    var zipCode: String? = null
)
