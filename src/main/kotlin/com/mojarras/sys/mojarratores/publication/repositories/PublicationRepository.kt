package com.mojarras.sys.mojarratores.publication.repositories

import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PublicationRepository : JpaRepository<PublicationEntity, Long> {

    fun findAllByOwnerId(ownerId: Long): List<PublicationEntity>

    fun findAllByStatus(status: PublicationStatus): List<PublicationEntity>

}