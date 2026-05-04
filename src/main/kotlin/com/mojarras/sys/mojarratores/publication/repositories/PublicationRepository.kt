package com.mojarras.sys.mojarratores.publication.repositories

import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PublicationRepository : JpaRepository<PublicationEntity, Long>, JpaSpecificationExecutor<PublicationEntity>