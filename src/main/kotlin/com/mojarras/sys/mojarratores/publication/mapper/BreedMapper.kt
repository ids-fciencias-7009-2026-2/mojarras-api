package com.mojarras.sys.mojarratores.publication.mapper

import com.mojarras.sys.mojarratores.publication.domain.BreedInfo
import com.mojarras.sys.mojarratores.publication.entities.BreedInfoEntity

fun BreedInfoEntity.toDomain() = BreedInfo(
    id = id,
    type = type,
    breedName = breedName,
    temperament = temperament,
    origin = origin,
    lifeSpan = lifeSpan,
    description = description
)