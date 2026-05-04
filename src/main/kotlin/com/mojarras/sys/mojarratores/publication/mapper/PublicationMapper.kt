package com.mojarras.sys.mojarratores.publication.mapper

import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.dto.request.CreatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationResponse
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationWithOnePhotoResponse
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationWithPhotosResponse
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity

// Request → Domain
fun CreatePublicationRequest.toPublication(ownerId: Long) = Publication(
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode,
    status = PublicationStatus.DRAFT
)

// Domain → Entity
fun Publication.toPublicationEntity() = PublicationEntity(
    ownerId = ownerId,
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode,
    status = status
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
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Domain → Response
fun Publication.toPublicationResponse() = PublicationResponse(
    id = requireNotNull(id),
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode
)

// Domain(complement) → Response
fun Publication.toPublicationWithPhotosResponse(photos: List<String>) =
    PublicationWithPhotosResponse(
        id = requireNotNull(id),
        petName = petName,
        description = description,
        type = type,
        breed = breed,
        zipCode = zipCode,
        photos = photos
)

// Domain(complement) → Response
fun Publication.toPublicationWithOnePhotoResponse(photo: String?) =
    PublicationWithOnePhotoResponse(
        id = requireNotNull(id),
        petName = petName,
        type = type,
        breed = breed,
        zipCode = zipCode,
        thumbnail = photo
)