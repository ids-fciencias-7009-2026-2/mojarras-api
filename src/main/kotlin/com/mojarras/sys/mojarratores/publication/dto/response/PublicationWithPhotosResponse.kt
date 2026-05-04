package com.mojarras.sys.mojarratores.publication.dto.response

import com.mojarras.sys.mojarratores.publication.domain.PetType

data class PublicationWithPhotosResponse(
    val id: Long,
    val petName: String,
    val description: String,
    val type: PetType,
    val breed: String?,
    val zipCode: String,
    val photos: List<String>
)