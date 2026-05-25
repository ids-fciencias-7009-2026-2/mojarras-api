package com.mojarras.sys.mojarratores.publication.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ExternalBreedResponse(

    val name: String,

    val temperament: String?,

    val origin: String?,

    @JsonProperty("life_span")
    val lifeSpan: String?,

    val description: String?
)