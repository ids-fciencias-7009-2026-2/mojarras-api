package com.mojarras.sys.mojarratores.publication.dto.request

import com.mojarras.sys.mojarratores.publication.domain.PetType
import jakarta.validation.constraints.Size

data class UpdatePublicationRequest(

    val petName: String?,
    @field:Size(min = 10, message = "Description too short")
    val description: String?,
    val type: PetType?,
    val breed: String?,
    val zipCode: String?

)