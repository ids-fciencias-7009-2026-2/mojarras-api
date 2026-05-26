package com.mojarras.sys.mojarratores.publication.dto.response

data class BreedInfoResponse(
    val breedName: String,
    val temperament: String?,
    val origin: String?,
    val lifeSpan: String?,
    val description: String?
)