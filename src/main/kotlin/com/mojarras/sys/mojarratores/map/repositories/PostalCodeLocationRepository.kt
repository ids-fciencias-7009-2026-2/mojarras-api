package com.mojarras.sys.mojarratores.map.repositories

import com.mojarras.sys.mojarratores.map.entities.PostalCodeLocationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostalCodeLocationRepository : JpaRepository<PostalCodeLocationEntity, String> {

    fun findByZipCode(zipCode: String): PostalCodeLocationEntity?
}