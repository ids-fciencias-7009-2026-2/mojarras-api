package com.mojarras.sys.mojarratores.map.dto.response

import com.mojarras.sys.mojarratores.publication.domain.PetType

data class MapPublicationItem(
    val id: Long,
    val petName: String,
    val type: PetType,
    val breed: String?,
    val thumbnail: String?
)