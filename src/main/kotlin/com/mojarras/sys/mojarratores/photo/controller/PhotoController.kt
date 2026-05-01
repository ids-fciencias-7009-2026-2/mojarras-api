package com.mojarras.sys.mojarratores.photo.controller

import com.mojarras.sys.mojarratores.photo.services.PhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/publications/{id}/photos")
class PhotoController(
    private val photoService: PhotoService
) {

    @PostMapping
    fun upload(
        @PathVariable id: Long,
        @RequestParam("files") files: List<MultipartFile>
    ): ResponseEntity<List<String>> {

        val photos = photoService.uploadPhotos(id, files)

        val urls = photos.map { it.url }

        return ResponseEntity.status(HttpStatus.CREATED).body(urls)
    }
}