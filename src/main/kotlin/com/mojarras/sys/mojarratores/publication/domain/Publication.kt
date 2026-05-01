package com.mojarras.sys.mojarratores.publication.domain

import java.time.LocalDateTime

data class Publication(
    val id: Long? = null,
    val ownerId: Long,
    val petName: String,
    val description: String,
    val type: String,
    val breed: String?,
    val zipCode: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
