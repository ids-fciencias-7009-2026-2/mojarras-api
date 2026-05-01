package com.mojarras.sys.mojarratores.publication.mapper

import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.dto.request.CreatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationResponse
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity

// Request → Domain
fun CreatePublicationRequest.toPublication(ownerId: Long) = Publication(
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)

// Domain → Entity
fun Publication.toPublicationEntity() = PublicationEntity(
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)

// Entity → Domain
fun PublicationEntity.toPublication() = Publication(
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
fun Publication.toPublicationResponse() = PublicationResponse(
    id = id ?: 0,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)