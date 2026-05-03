package com.mojarras.sys.mojarratores.publication.domain

import java.time.LocalDateTime

data class Publication(
    val id: Long? = null,
    val ownerId: Long,
    val petName: String,
    val description: String,
    val type: PetType,
    val breed: String?,
    val zipCode: String,
    val status: PublicationStatus = PublicationStatus.DRAFT,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

enum class PetType {
    DOG,
    CAT
}

enum class PublicationStatus {
    DRAFT,
    ACTIVE
}