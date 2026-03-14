package com.mojarras.sys.mojarratores.repositories

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.entities.UserEntity

fun User.toUserEntity(): UserEntity {
    return UserEntity(
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
