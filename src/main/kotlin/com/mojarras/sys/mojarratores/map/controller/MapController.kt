package com.mojarras.sys.mojarratores.map.controller

import com.mojarras.sys.mojarratores.map.dto.response.MapPublicationResponse
import com.mojarras.sys.mojarratores.map.dto.response.PublicationLocationResponse
import com.mojarras.sys.mojarratores.map.services.MapService
import com.mojarras.sys.mojarratores.publication.domain.PetType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/publications")
class MapController(
    private val mapService: MapService
) {

    @GetMapping("/map")
    fun getMap(
        @RequestParam(required = false) type: PetType?,
        @RequestParam(required = false) breed: String?,
        @RequestParam(required = false) zipCode: String?,
        authentication: Authentication
    ): ResponseEntity<List<MapPublicationResponse>> {

        val data = mapService.getMapData(
            type,
            breed,
            zipCode,
            authentication.name
        )

        return ResponseEntity.ok(data)
    }

    @GetMapping("/{id}/location")
    fun getPublicationLocation(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<PublicationLocationResponse> {

        val location = mapService.getPublicationLocation(id, authentication.name)

        return ResponseEntity.ok(location)
    }
}