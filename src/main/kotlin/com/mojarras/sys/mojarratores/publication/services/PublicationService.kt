package com.mojarras.sys.mojarratores.publication.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.mapper.toPublication
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationEntity
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.springframework.stereotype.Service

import org.slf4j.LoggerFactory

@Service
class PublicationService(
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository
) {

    private val logger = LoggerFactory.getLogger(PublicationService::class.java)

    fun create(publication: Publication, email: String): Publication {

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        validate(publication)

        val publicationEntity = publication.copy(ownerId = user.id!!).toPublicationEntity()

        val saved = publicationRepository.save(publicationEntity)

        logger.info("Publication created: ${saved.id} by user ${user.email}")

        return saved.toPublication()
    }

    fun getById(id: Long): Publication {

        val entity = publicationRepository.findById(id)
            .orElseThrow { NotFoundException("Publication not found") }

        return entity.toPublication()
    }

    fun getAll(): List<Publication> {
        return publicationRepository.findAll().map { it.toPublication() }
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