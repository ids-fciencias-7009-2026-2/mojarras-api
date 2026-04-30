package com.mojarras.sys.mojarratores.mapper

import com.mojarras.sys.mojarratores.domain.User
import com.mojarras.sys.mojarratores.dto.request.CreateUserRequest
import com.mojarras.sys.mojarratores.dto.response.UserResponse
import com.mojarras.sys.mojarratores.entities.UserEntity

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
