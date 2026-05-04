package com.mojarras.sys.mojarratores.interest.repositories

import com.mojarras.sys.mojarratores.interest.entities.InterestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface InterestRepository : JpaRepository<InterestEntity, Long> {

    fun existsByPublicationIdAndInterestedUserId(
        publicationId: Long,
        interestedUserId: Long
    ): Boolean

    fun findAllByPublicationId(publicationId: Long): List<InterestEntity>
}