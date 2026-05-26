package com.mojarras.sys.mojarratores.publication.repositories

import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.entities.BreedInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BreedInfoRepository : JpaRepository<BreedInfoEntity, Long> {

    fun findByBreedNameIgnoreCaseAndType(
        breedName: String,
        type: PetType
    ): BreedInfoEntity?

    fun findAllByType(type: PetType): List<BreedInfoEntity>
}