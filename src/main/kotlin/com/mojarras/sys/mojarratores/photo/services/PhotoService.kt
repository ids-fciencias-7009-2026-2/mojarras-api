package com.mojarras.sys.mojarratores.photo.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.exception.UnauthorizedException
import com.mojarras.sys.mojarratores.infrastructure.CloudinaryService
import com.mojarras.sys.mojarratores.photo.domain.Photo
import com.mojarras.sys.mojarratores.photo.mapper.toPhoto
import com.mojarras.sys.mojarratores.photo.mapper.toPhotoEntity
import com.mojarras.sys.mojarratores.photo.repositories.PhotoRepository
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    private val cloudinaryService: CloudinaryService
) {

    private val logger = LoggerFactory.getLogger(PhotoService::class.java)

    @Transactional
    fun uploadPhotos(
        publicationId: Long,
        email: String,
        files: List<MultipartFile>
    ): List<Photo> {

        val publication = publicationRepository.findById(publicationId)
            .orElseThrow { NotFoundException("Publication not found") }

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        if (publication.ownerId != user.id) {
            throw UnauthorizedException("Not your publication")
        }

        if (files.isEmpty()) {
            throw BadRequestException("At least one photo is required")
        }

        val photos = files.map { file ->

            val url = cloudinaryService.upload(file)

            Photo(
                publicationId = publication.id!!,
                url = url
            )
        }

        val savedPhotos = photoRepository.saveAll(photos.map { it.toPhotoEntity() })

        if (publication.status == PublicationStatus.DRAFT) {
            val updated = publication.copy(status = PublicationStatus.ACTIVE)
            publicationRepository.save(updated)
        }

        logger.info("Photos uploaded for publication: $publicationId")

        return savedPhotos .map { it.toPhoto() }
    }

    fun getByPublication(publicationId: Long): List<Photo> {
        return photoRepository.findAllByPublicationId(publicationId)
            .map { it.toPhoto() }
    }
}