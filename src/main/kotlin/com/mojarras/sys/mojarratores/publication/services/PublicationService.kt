package com.mojarras.sys.mojarratores.publication.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.photo.repositories.PhotoRepository
import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.mapper.toPublication
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationEntity
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.springframework.stereotype.Service

import org.slf4j.LoggerFactory

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

        validate(publication)

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

    fun getAll(): List<Pair<Publication, String?>> {

        val publications = publicationRepository.findAllByStatus(PublicationStatus.ACTIVE)

        return publications.map { entity ->

            val photo = photoRepository
                .findTopByPublicationIdOrderByIdAsc(entity.id!!)

            Pair(entity.toPublication(), photo?.url)
        }
    }

    private fun validate(publication: Publication) {

        if (publication.type !in listOf("DOG", "CAT")) {
            throw BadRequestException("Type must be DOG or CAT")
        }

        if (publication.description.length < 10) {
            throw BadRequestException("Description too short")
        }
    }
}