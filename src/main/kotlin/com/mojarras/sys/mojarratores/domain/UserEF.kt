package com.mojarras.sys.mojarratores.domain

import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import java.util.UUID

fun CreateUserRequest.toUser(): User {

    return User(
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        zipCode = this.zipCode,
        password = this.password
    )
}