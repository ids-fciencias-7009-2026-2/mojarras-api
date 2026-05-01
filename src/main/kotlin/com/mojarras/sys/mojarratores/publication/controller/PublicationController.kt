package com.mojarras.sys.mojarratores.publication.controller

import com.mojarras.sys.mojarratores.publication.dto.request.CreatePublicationRequest
import com.mojarras.sys.mojarratores.publication.dto.response.PublicationResponse
import com.mojarras.sys.mojarratores.publication.mapper.toPublication
import com.mojarras.sys.mojarratores.publication.mapper.toPublicationResponse
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

    @PostMapping("/create")
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
    fun getById(@PathVariable id: Long): ResponseEntity<PublicationResponse> {

        val publication = publicationService.getById(id)

        return ResponseEntity.ok(publication.toPublicationResponse())
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<PublicationResponse>> {

        val list = publicationService.getAll()
            .map { it.toPublicationResponse() }

        return ResponseEntity.ok(list)
    }
}