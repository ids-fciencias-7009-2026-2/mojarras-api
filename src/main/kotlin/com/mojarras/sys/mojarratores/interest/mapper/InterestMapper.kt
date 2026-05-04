package com.mojarras.sys.mojarratores.interest.mapper

import com.mojarras.sys.mojarratores.interest.domain.Interest
import com.mojarras.sys.mojarratores.interest.entities.InterestEntity

fun Interest.toInterestEntity() = InterestEntity(
    publicationId = publicationId,
    interestedUserId = interestedUserId
)

fun InterestEntity.toInterest() = Interest(
    id = id,
    publicationId = publicationId,
    interestedUserId = interestedUserId,
    createdAt = createdAt
)