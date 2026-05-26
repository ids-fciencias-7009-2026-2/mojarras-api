package com.mojarras.sys.mojarratores.map.dto.response

data class ZippopotamResponse(
    val places: List<Place>
)

data class Place(
    val latitude: String,
    val longitude: String
)