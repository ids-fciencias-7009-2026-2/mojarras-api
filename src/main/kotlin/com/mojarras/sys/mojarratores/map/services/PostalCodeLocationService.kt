package com.mojarras.sys.mojarratores.map.services

import com.mojarras.sys.mojarratores.map.client.ZippopotamClient
import com.mojarras.sys.mojarratores.map.domain.PostalCodeLocation
import com.mojarras.sys.mojarratores.map.mapper.toDomain
import com.mojarras.sys.mojarratores.map.mapper.toEntity
import com.mojarras.sys.mojarratores.map.repositories.PostalCodeLocationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PostalCodeLocationService(
    private val repository: PostalCodeLocationRepository,
    private val zippopotamClient: ZippopotamClient
) {

    private val logger = LoggerFactory.getLogger(PostalCodeLocationService::class.java)

    fun getOrCreate(zipCode: String): PostalCodeLocation {

        val existing = repository.findById(zipCode)

        if (existing.isPresent) {
            return existing.get().toDomain()
        }

        logger.info("ZipCode not found locally, fetching from API: $zipCode")

        val (lat, lng) = zippopotamClient.getCoordinates(zipCode)

        val location = PostalCodeLocation(
            zipCode = zipCode,
            lat = lat,
            lng = lng
        )

        val saved = repository.save(location.toEntity())

        return saved.toDomain()
    }
}