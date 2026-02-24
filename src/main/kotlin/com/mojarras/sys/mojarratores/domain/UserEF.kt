package com.mojarras.sys.mojarratores.domain

import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import java.util.UUID

fun CreateUserRequest.toUser(): User {

    val idUser = "idUser-random-" + UUID.randomUUID().toString()

    return User(
        idUser = idUser,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        zipCode = this.zipCode,
        password = this.password
    )
}