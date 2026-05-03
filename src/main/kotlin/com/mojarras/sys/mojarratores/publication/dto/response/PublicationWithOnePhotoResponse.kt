package com.mojarras.sys.mojarratores.publication.dto.response

import com.mojarras.sys.mojarratores.publication.domain.PetType

data class PublicationWithOnePhotoResponse(
    val id: Long,
    val petName: String,
    val type: PetType,
    val breed: String?,
    val zipCode: String,
    val thumbnail: String?
)