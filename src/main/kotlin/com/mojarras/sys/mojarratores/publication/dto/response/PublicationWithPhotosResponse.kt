package com.mojarras.sys.mojarratores.publication.dto.response

data class PublicationWithPhotosResponse(
    val id: Long,
    val petName: String,
    val description: String,
    val type: String,
    val breed: String?,
    val zipCode: String,
    val photos: List<String>
)