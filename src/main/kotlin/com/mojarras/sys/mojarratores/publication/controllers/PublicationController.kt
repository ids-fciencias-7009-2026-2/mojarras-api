package com.mojarras.sys.mojarratores.publication.controllers

import com.mojarras.sys.mojarratores.publication.dto.request.CreatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationResponse
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationWithOnePhotoResponse
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationWithPhotosResponse
import com.mojarras.sys.mojarratores.publication.mapper.toPublication
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationResponse
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationWithOnePhotoResponse
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationWithPhotosResponse
import com.mojarras.sys.mojarratores.publication.services.PublicationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

@RestController
@RequestMapping("/publications")
class PublicationController(
    private val publicationService: PublicationService
) {

    private val logger = LoggerFactory.getLogger(PublicationController::class.java)

    @PostMapping()
    fun create(
        @Valid @RequestBody request: CreatePublicationRequest,
        authentication: Authentication
    ): ResponseEntity<PublicationResponse> {

        val publication = request.toPublication(0) // owner se asigna en service

        val created = publicationService.create(publication, authentication.name)

        logger.info("Publication created: ${created.id}")

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(created.toPublicationResponse())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<PublicationWithPhotosResponse> {

        val (publication, photos) = publicationService.getById(id)

        return ResponseEntity.ok(
            publication.toPublicationWithPhotosResponse(photos)
        )
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<PublicationWithOnePhotoResponse>> {

        val list = publicationService.getAll()

        val response = list.map { (publication, thumbnail) ->
            publication.toPublicationWithOnePhotoResponse(thumbnail)

        }

        return ResponseEntity.ok(response)
    }
}