package com.mojarras.sys.mojarratores.domain

import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import java.util.UUID

fun CreateUserRequest.toUser(): User {

    return User(
        email = this.email,
        password = this.password,
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        zipCode = this.zipCode
    )
}