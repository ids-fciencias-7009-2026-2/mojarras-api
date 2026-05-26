package com.mojarras.sys.mojarratores.map.dto.response

data class MapPublicationResponse(
    val zipCode: String,
    val lat: Double,
    val lng: Double,
    val count: Int,
    val publications: List<MapPublicationItem>
)