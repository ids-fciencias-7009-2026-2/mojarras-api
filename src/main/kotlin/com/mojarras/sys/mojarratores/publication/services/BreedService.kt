package com.mojarras.sys.mojarratores.publication.services

import com.mojarras.sys.mojarratores.breed.client.CatApiClient
import com.mojarras.sys.mojarratores.breed.client.DogApiClient
import com.mojarras.sys.mojarratores.publication.domain.BreedInfo
import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.dto.response.ExternalBreedResponse
import com.mojarras.sys.mojarratores.publication.entities.BreedInfoEntity
import com.mojarras.sys.mojarratores.publication.mapper.toDomain
import com.mojarras.sys.mojarratores.publication.repositories.BreedInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BreedService(
    private val dogApiClient: DogApiClient,
    private val catApiClient: CatApiClient,
    private val breedRepository: BreedInfoRepository
) {

    private val logger = LoggerFactory.getLogger(BreedService::class.java)

    private var dogBreedsCache: List<ExternalBreedResponse>? = null
    private var catBreedsCache: List<ExternalBreedResponse>? = null

    private fun getDogBreedsCached(): List<ExternalBreedResponse> {
        if (dogBreedsCache != null) return dogBreedsCache!!

        val data = dogApiClient.getBreeds()

        if (data.isEmpty()) {
            logger.warn("Dog API returned empty, using empty cache")
        }

        dogBreedsCache = data
        return data
    }

    private fun getCatBreedsCached(): List<ExternalBreedResponse> {
        if (catBreedsCache != null) return catBreedsCache!!

        val data = catApiClient.getBreeds()

        if (data.isEmpty()) {
            logger.warn("Cat API returned empty, using empty cache")
        }

        catBreedsCache = data
        return data
    }

    fun getOrCreateBreedInfo(type: PetType, breedName: String?): BreedInfo? {

        if (breedName.isNullOrBlank()) return null

        val existing = breedRepository
            .findByBreedNameIgnoreCaseAndType(breedName, type)

        if (existing != null) return existing.toDomain()

        val apiList = when (type) {
            PetType.DOG -> getDogBreedsCached()
            PetType.CAT -> getCatBreedsCached()
        }

        val apiData = apiList.find {
            it.name.equals(breedName, ignoreCase = true)
        }

        if (apiData == null) {
            logger.warn("Breed not found anywhere: $breedName")
            return null
        }

        val saved = breedRepository.save(
            BreedInfoEntity(
                type = type,
                breedName = apiData.name,
                temperament = apiData.temperament,
                origin = apiData.origin,
                lifeSpan = apiData.lifeSpan,
                description = apiData.description
            )
        )

        logger.info("Breed saved: ${saved.breedName}")

        return saved.toDomain()
    }

    fun getBreedNames(type: PetType): List<String> {
        val apiList = when (type) {
            PetType.DOG -> getDogBreedsCached()
            PetType.CAT -> getCatBreedsCached()
        }

        if (apiList.isEmpty()) {
            logger.warn("API unavailable, fallback to DB")

            return breedRepository.findAllByType(type)
                .map { it.breedName }
        }

        return apiList.map { it.name }
    }
}