package com.mojarras.sys.mojarratores.publication.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.exception.UnauthorizedException
import com.mojarras.sys.mojarratores.map.services.PostalCodeLocationService
import com.mojarras.sys.mojarratores.photo.repositories.PhotoRepository
import com.mojarras.sys.mojarratores.photo.services.PhotoService
import com.mojarras.sys.mojarratores.publication.domain.BreedInfo
import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.domain.Publication
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.dto.request.UpdatePublicationRequest
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity
import com.mojarras.sys.mojarratores.publication.mapper.toDomain
import com.mojarras.sys.mojarratores.publication.mapper.toPublication
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationEntity
import com.mojarras.sys.mojarratores.publication.repositories.BreedInfoRepository
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
    private val photoService: PhotoService,
    private val photoRepository: PhotoRepository,
    private val postalCodeLocationService: PostalCodeLocationService,
    private val breedService: BreedService,
    private val breedRepository: BreedInfoRepository
) {

    private val logger = LoggerFactory.getLogger(PublicationService::class.java)

    fun create(publication: Publication, email: String): Publication {

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        postalCodeLocationService.getOrCreate(publication.zipCode)

        val breedInfo = breedService.getOrCreateBreedInfo(
            publication.type,
            publication.breed
        )

        val publicationEntity = publication.copy(
            ownerId = user.id!!,
            status = PublicationStatus.DRAFT
        ).toPublicationEntity().copy(
            breedInfoId = breedInfo?.id
        )

        val saved = publicationRepository.save(publicationEntity)

        logger.info("Publication created: ${saved.id} by user ${user.email}")

        return saved.toPublication()
    }

    fun getById(id: Long): Triple<Publication, List<String>, BreedInfo?> {

        val publication = publicationRepository.findById(id)
            .orElseThrow { NotFoundException("Publication not found") }

        if (publication.status != PublicationStatus.ACTIVE) {
            throw NotFoundException("Publication not available")
        }

        val photos = photoRepository.findAllByPublicationId(id)
            .map { it.url }

        val breedInfo = publication.breedInfoId?.let {
            breedRepository.findById(it).orElse(null)?.toDomain()
        }

        return Triple(publication.toPublication(), photos, breedInfo)
    }

    fun getAll(
        type: PetType?,
        zipCode: String?,
        breed: String?,
        pageable: Pageable,
        email: String?
    ): Page<Pair<Publication, String?>> {

        var spec: Specification<PublicationEntity> =
            PublicationSpecification.hasStatus(PublicationStatus.ACTIVE)

        val userId = email?.let {
            userRepository.findByEmail(it)?.id
        }

        PublicationSpecification.isNotOwner(userId)?.let {
            spec = spec.and(it)
        }

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

    fun getMyPublications(
        email: String,
        pageable: Pageable
    ): Page<Pair<Publication, String?>> {

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        val spec = PublicationSpecification.isOwner(user.id!!)

        val page = publicationRepository.findAll(spec, pageable)

        return page.map { entity ->
            val photo = photoRepository
                .findTopByPublicationIdOrderByIdAsc(entity.id!!)

            Pair(entity.toPublication(), photo?.url)
        }
    }

    fun update(id: Long, email: String, request: UpdatePublicationRequest): Publication {

        val existing = publicationRepository.findById(id)
            .orElseThrow { NotFoundException("Publication not found") }

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        if (existing.ownerId != user.id) {
            throw UnauthorizedException("Not your publication")
        }

        val newType = request.type ?: existing.type
        val newBreed = request.breed ?: existing.breed

        val breedChanged =
            newType != existing.type || newBreed != existing.breed

        val newBreedInfoId = if (breedChanged) {
            val breedInfo = breedService.getOrCreateBreedInfo(newType, newBreed)
            breedInfo?.id
        } else {
            existing.breedInfoId
        }

        val updated = existing.copy(
            petName     = request.petName     ?: existing.petName,
            description = request.description ?: existing.description,
            type        = newType,
            breed       = newBreed,
            zipCode     = request.zipCode     ?: existing.zipCode,
            breedInfoId = newBreedInfoId
        )

        val saved = publicationRepository.save(updated)

        logger.info("Publication updated: $id by user ${user.email}")

        return saved.toPublication()
    }

    fun delete(id: Long, email: String) {

        val publication = publicationRepository.findById(id)
            .orElseThrow { NotFoundException("Publication not found") }

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        if (publication.ownerId != user.id) {
            throw UnauthorizedException("Not your publication")
        }

        photoService.deleteAllByPublication(id)

        publicationRepository.deleteById(id)

        logger.info("Publication deleted: $id by user ${user.email}")
    }
}