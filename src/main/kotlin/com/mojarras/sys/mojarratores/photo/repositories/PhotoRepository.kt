package com.mojarras.sys.mojarratores.photo.repositories

import com.mojarras.sys.mojarratores.photo.entities.PhotoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<PhotoEntity, Long> {

    fun findAllByPublicationId(publicationId: Long): List<PhotoEntity>

    fun findTopByPublicationIdOrderByIdAsc(publicationId: Long): PhotoEntity?

}