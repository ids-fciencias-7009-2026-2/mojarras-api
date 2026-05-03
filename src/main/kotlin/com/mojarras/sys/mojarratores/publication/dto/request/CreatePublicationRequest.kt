package com.mojarras.sys.mojarratores.publication.dto.request

import com.mojarras.sys.mojarratores.publication.domain.PetType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreatePublicationRequest(

    @field:NotBlank
    val petName: String,

    @field:NotBlank
    @field:Size(min = 10, message = "Description too short")
    val description: String,

    @field:NotNull
    val type: PetType,

    val breed: String?,

    @field:NotBlank
    val zipCode: String
)