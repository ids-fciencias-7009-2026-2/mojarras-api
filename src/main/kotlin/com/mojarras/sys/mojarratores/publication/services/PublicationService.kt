package com.mojarras.sys.mojarratores.publication.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.photo.repositories.PhotoRepository
import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity
import com.mojarras.sys.mojarratores.publication.mapper.toPublication
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationEntity
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import com.mojarras.sys.mojarratores.publication.repositories.PublicationSpecification
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.springframework.stereotype.Service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification

@Service
class PublicationService(
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository
) {

    private val logger = LoggerFactory.getLogger(PublicationService::class.java)

    fun create(publication: Publication, email: String): Publication {

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        val publicationEntity = publication.copy(
            ownerId = user.id!!,
            status = PublicationStatus.DRAFT
        ).toPublicationEntity()

        val saved = publicationRepository.save(publicationEntity)

        logger.info("Publication created: ${saved.id} by user ${user.email}")

        return saved.toPublication()
    }

    fun getById(id: Long): Pair<Publication, List<String>> {

        val publication = publicationRepository.findById(id)
            .orElseThrow { NotFoundException("Publication not found") }

        if (publication.status != PublicationStatus.ACTIVE) {
            throw NotFoundException("Publication not available")
        }

        val photos = photoRepository.findAllByPublicationId(id)
            .map { it.url }

        return Pair(publication.toPublication(), photos)
    }

    fun getAll(
        type: PetType?,
        zipCode: String?,
        breed: String?,
        pageable: Pageable
    ): Page<Pair<Publication, String?>> {

        var spec: Specification<PublicationEntity> =
            PublicationSpecification.hasStatus(PublicationStatus.ACTIVE)

        PublicationSpecification.hasType(type)?.let {
            spec = spec.and(it)
        }

        PublicationSpecification.hasZipCode(zipCode)?.let {
            spec = spec.and(it)
        }

        PublicationSpecification.hasBreed(breed)?.let {
            spec = spec.and(it)
        }

        val page = publicationRepository.findAll(spec, pageable)

        return page.map { entity ->

            val photo = photoRepository
                .findTopByPublicationIdOrderByIdAsc(entity.id!!)

            Pair(entity.toPublication(), photo?.url)
        }
    }
}