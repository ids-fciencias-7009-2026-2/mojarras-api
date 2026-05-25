package com.mojarras.sys.mojarratores.map.client

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.map.dto.response.ZippopotamResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ZippopotamClient(
    private val restTemplate: RestTemplate = RestTemplate()
) {

    fun getCoordinates(zipCode: String): Pair<Double, Double> {

        // De momento solo soporte a méxico
        val url = "http://api.zippopotam.us/mx/$zipCode"

        return try {
            val response = restTemplate.getForObject(url, ZippopotamResponse::class.java)
                ?: throw NotFoundException("Zip code not found")

            if (response.places.isEmpty()) {
                throw NotFoundException("Zip code not found")
            }

            val place = response.places.first()

            Pair(place.latitude.toDouble(), place.longitude.toDouble())

        } catch (ex: Exception) {
            throw BadRequestException("Invalid or non-existent zip code")
        }
    }
}