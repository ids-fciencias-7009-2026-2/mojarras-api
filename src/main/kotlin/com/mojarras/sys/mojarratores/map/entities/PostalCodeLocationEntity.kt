package com.mojarras.sys.mojarratores.map.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "postal_code_locations")
data class PostalCodeLocationEntity(

    @Id
    @Column(name = "zip_code")
    val zipCode: String,

    @Column(nullable = false)
    val lat: Double,

    @Column(nullable = false)
    val lng: Double
)