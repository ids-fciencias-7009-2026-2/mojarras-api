package com.mojarras.sys.mojarratores.map.services

import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.map.dto.response.MapPublicationItem
import com.mojarras.sys.mojarratores.map.dto.response.MapPublicationResponse
import com.mojarras.sys.mojarratores.map.repositories.PostalCodeLocationRepository
import com.mojarras.sys.mojarratores.photo.repositories.PhotoRepository
import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import com.mojarras.sys.mojarratores.publication.repositories.PublicationSpecification
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MapService(
    private val publicationRepository: PublicationRepository,
    private val photoRepository: PhotoRepository,
    private val postalCodeLocationRepository: PostalCodeLocationRepository,
    private val userRepository: UserRepository
) {

    private val logger = LoggerFactory.getLogger(MapService::class.java)

    fun getMapData(
        type: PetType?,
        breed: String?,
        zipCode: String?,
        userEmail: String
    ): List<MapPublicationResponse> {

        val user = userRepository.findByEmail(userEmail)
            ?: throw NotFoundException("User not found")

        var spec = PublicationSpecification.hasStatus(PublicationStatus.ACTIVE)

        PublicationSpecification.hasType(type)?.let {
            spec = spec.and(it)
        }

        PublicationSpecification.hasBreed(breed)?.let {
            spec = spec.and(it)
        }

        PublicationSpecification.hasZipCode(zipCode)?.let {
            spec = spec.and(it)
        }

        PublicationSpecification.isNotOwner(user.id)?.let {
            spec = spec.and(it)
        }

        val publications = publicationRepository.findAll(spec)

        if (publications.isEmpty()) return emptyList()

        val grouped = publications.groupBy { it.zipCode }

        val result = grouped.mapNotNull { (zip, pubs) ->

            val location = postalCodeLocationRepository.findByZipCode(zip)
                ?: run {
                    logger.warn("No location for zipCode: $zip")
                    return@mapNotNull null
                }

            val items = pubs.map { pub ->

                val photo = photoRepository
                    .findTopByPublicationIdOrderByIdAsc(pub.id!!)

                MapPublicationItem(
                    id = pub.id!!,
                    petName = pub.petName,
                    type = pub.type,
                    breed = pub.breed,
                    thumbnail = photo?.url
                )
            }

            MapPublicationResponse(
                zipCode = zip,
                lat = location.lat,
                lng = location.lng,
                count = items.size,
                publications = items
            )
        }

        logger.info("Map data generated with filters → ${result.size} clusters")

        return result
    }
}