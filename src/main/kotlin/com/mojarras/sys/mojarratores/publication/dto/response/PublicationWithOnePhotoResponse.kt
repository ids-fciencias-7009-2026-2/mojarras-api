package com.mojarras.sys.mojarratores.publication.dto.response

data class PublicationWithOnePhotoResponse(
    val id: Long,
    val petName: String,
    val type: String,
    val breed: String?,
    val zipCode: String,
    val thumbnail: String?
)