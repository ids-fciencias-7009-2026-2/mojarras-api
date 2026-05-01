package com.mojarras.sys.mojarratores.publication.dto.request

import jakarta.validation.constraints.NotBlank

data class CreatePublicationRequest(

    @field:NotBlank
    val petName: String,

    @field:NotBlank
    val description: String,

    @field:NotBlank
    val type: String,

    val breed: String?,

    @field:NotBlank
    val zipCode: String
)