package com.mojarras.sys.mojarratores.interest.domain

import java.time.LocalDateTime

data class Interest(
    val id: Long? = null,
    val publicationId: Long,
    val interestedUserId: Long,
    val createdAt: LocalDateTime? = null
)