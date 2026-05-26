package com.mojarras.sys.mojarratores.publication.controllers

import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.services.BreedService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/breeds")
class BreedController(
    private val breedService: BreedService
) {

    @GetMapping
    fun getBreeds(
        @RequestParam type: PetType
    ): ResponseEntity<List<String>> {

        val breeds = breedService.getBreedNames(type)

        return ResponseEntity.ok(breeds)
    }
}