package com.mojarras.sys.mojarratores.publication.mapper

import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.dto.request.CreatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationResponse
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity

// Request → Domain
fun CreatePublicationRequest.toDomain(ownerId: Long) = Publication(
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)

// Domain → Entity
fun Publication.toEntity() = PublicationEntity(
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)

// Entity → Domain
fun PublicationEntity.toDomain() = Publication(
    id = id,
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Domain → Response
fun Publication.toResponse() = PublicationResponse(
    id = id ?: 0,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)