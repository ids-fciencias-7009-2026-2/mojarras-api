package com.mojarras.sys.mojarratores.map.mapper

import com.mojarras.sys.mojarratores.map.domain.PostalCodeLocation
import com.mojarras.sys.mojarratores.map.entities.PostalCodeLocationEntity

fun PostalCodeLocationEntity.toDomain() = PostalCodeLocation(
    zipCode = zipCode,
    lat = lat,
    lng = lng
)

fun PostalCodeLocation.toEntity() = PostalCodeLocationEntity(
    zipCode = zipCode,
    lat = lat,
    lng = lng
)