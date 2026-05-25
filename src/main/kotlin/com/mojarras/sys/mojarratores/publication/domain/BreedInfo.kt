package com.mojarras.sys.mojarratores.publication.domain

data class BreedInfo(
    val id: Long? = null,
    val type: PetType,
    val breedName: String,
    val temperament: String?,
    val origin: String?,
    val lifeSpan: String?,
    val description: String?
)