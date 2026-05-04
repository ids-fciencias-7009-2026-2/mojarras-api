package com.mojarras.sys.mojarratores.publication.repositories

import com.mojarras.sys.mojarratores.publication.domain.PetType
import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import com.mojarras.sys.mojarratores.publication.entities.PublicationEntity
import org.springframework.data.jpa.domain.Specification

object PublicationSpecification {

    fun hasStatus(status: PublicationStatus): Specification<PublicationEntity> {
        return Specification { root, _, cb ->
            cb.equal(root.get<PublicationStatus>("status"), status)
        }
    }

    fun hasType(type: PetType?): Specification<PublicationEntity>? {
        return type?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<PetType>("type"), it)
            }
        }
    }

    fun hasZipCode(zipCode: String?): Specification<PublicationEntity>? {
        return zipCode?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<String>("zipCode"), it)
            }
        }
    }

    fun hasBreed(breed: String?): Specification<PublicationEntity>? {
        return breed?.let {
            Specification { root, _, cb ->
                cb.equal(cb.upper(root.get("breed")), it.uppercase())
            }
        }
    }
}