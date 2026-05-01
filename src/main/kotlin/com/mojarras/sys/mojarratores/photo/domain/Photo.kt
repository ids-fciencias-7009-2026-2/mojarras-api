package com.mojarras.sys.mojarratores.photo.domain

data class Photo(
    val id: Long? = null,
    val publicationId: Long,
    val url: String
)