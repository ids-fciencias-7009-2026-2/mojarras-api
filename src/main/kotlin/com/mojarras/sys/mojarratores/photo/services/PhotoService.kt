package com.mojarras.sys.mojarratores.photo.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.infrastructure.CloudinaryService
import com.mojarras.sys.mojarratores.photo.domain.Photo
import com.mojarras.sys.mojarratores.photo.mapper.toPhoto
import com.mojarras.sys.mojarratores.photo.mapper.toPhotoEntity
import com.mojarras.sys.mojarratores.photo.repositories.PhotoRepository
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val publicationRepository: PublicationRepository,
    private val cloudinaryService: CloudinaryService
) {

    private val logger = LoggerFactory.getLogger(PhotoService::class.java)

    fun uploadPhotos(publicationId: Long, files: List<MultipartFile>): List<Photo> {

        val publication = publicationRepository.findById(publicationId)
            .orElseThrow { NotFoundException("Publication not found") }

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

        val saved = photoRepository.saveAll(photos.map { it.toPhotoEntity() })

        logger.info("Photos uploaded for publication: $publicationId")

        return saved.map { it.toPhoto() }
    }

    fun getByPublication(publicationId: Long): List<Photo> {
        return photoRepository.findAllByPublicationId(publicationId)
            .map { it.toPhoto() }
    }
}