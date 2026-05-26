package com.mojarras.sys.mojarratores.breed.client

import com.fasterxml.jackson.annotation.JsonProperty
import com.mojarras.sys.mojarratores.publication.dto.response.ExternalBreedResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DogApiClient(
    private val restTemplate: RestTemplate,
    @Value("\${external.dog-api.base-url}") private val baseUrl: String,
    @Value("\${external.dog-api.api-key}") private val apiKey: String
) {

    private val logger = LoggerFactory.getLogger(DogApiClient::class.java)

    fun getBreeds(): List<ExternalBreedResponse> {
        return try{
            val headers = HttpHeaders().apply {
                set("x-api-key", apiKey)
            }

            val entity = HttpEntity<Void>(headers)

            val response = restTemplate.exchange(
                "$baseUrl/breeds",
                HttpMethod.GET,
                entity,
                Array<ExternalBreedResponse>::class.java
            )

            response.body?.toList() ?: emptyList()

        } catch (ex: Exception) {
        logger.error("Dog API failed", ex)
        emptyList()
        }
    }
}