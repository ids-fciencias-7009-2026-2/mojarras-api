package com.mojarras.sys.mojarratores.photo.mapper

import com.mojarras.sys.mojarratores.photo.domain.Photo
import com.mojarras.sys.mojarratores.photo.entities.PhotoEntity

fun Photo.toPhotoEntity() = PhotoEntity(
    publicationId = publicationId,
    url = url
)

fun PhotoEntity.toPhoto() = Photo(
    id = id,
    publicationId = publicationId,
    url = url
)