package com.mojarras.sys.mojarratores.user.mapper

import com.mojarras.sys.mojarratores.user.domain.User
import com.mojarras.sys.mojarratores.user.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.user.dto.response.UserResponse
import com.mojarras.sys.mojarratores.user.entities.UserEntity

// DTO → Domain
fun CreateUserRequest.toDomain() = User(
    email = email,
    password = password,
    username = username,
    firstName = firstName,
    lastName = lastName,
    zipCode = zipCode
)

// Domain → Entity
fun User.toEntity(passwordHash: String) = UserEntity(
    email = email,
    passwordHash = passwordHash,
    username = username,
    firstName = firstName,
    lastName = lastName,
    zipCode = zipCode
)

// Entity → Domain
fun UserEntity.toDomain() = User(
    id = id,
    email = email,
    password = passwordHash,
    username = username,
    firstName = firstName,
    lastName = lastName,
    zipCode = zipCode
)

// Domain → Response
fun User.toResponse() = UserResponse(
    id = id ?: 0L,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    zipCode = zipCode
)
