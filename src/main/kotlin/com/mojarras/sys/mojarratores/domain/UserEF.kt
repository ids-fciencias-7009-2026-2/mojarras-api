package com.mojarras.sys.mojarratores.domain

import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.dto.request.UpdateUserRequest
import com.mojarras.sys.mojarratores.entities.UserEntity
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

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        email = this.email,
        password = this.password,
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        zipCode = this.zipCode,
        token = this.token
    )
}

