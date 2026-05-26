package com.mojarras.sys.mojarratores.publication.mapper

import com.mojarras.sys.mojarratores.photo.dto.response.PhotoResponse
import com.mojarras.sys.mojarratores.publication.domain.BreedInfo
import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.dto.request.CreatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.request.UpdatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.response.BreedInfoResponse
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
fun Publication.toPublicationWithPhotosResponse(
    photos: List<PhotoResponse>,
    breedInfo: BreedInfo?
) = PublicationWithPhotosResponse(
    id = requireNotNull(id),
    petName = petName,
    description = description,
    type = type,
    breed = breed,
    zipCode = zipCode,
    photos = photos,
    breedInfo = breedInfo?.let {
        BreedInfoResponse(
            breedName = it.breedName,
            temperament = it.temperament,
            origin = it.origin,
            lifeSpan = it.lifeSpan,
            description = it.description
        )
    }
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

// Request -> Domain
fun UpdatePublicationRequest.applyTo(existing: Publication) = existing.copy(
    petName = petName ?: existing.petName,
    description = description ?: existing.description,
    type = type ?: existing.type,
    breed = breed ?: existing.breed,
    zipCode = zipCode ?: existing.zipCode
)
