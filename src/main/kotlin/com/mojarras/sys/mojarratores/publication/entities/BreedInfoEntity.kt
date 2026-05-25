package com.mojarras.sys.mojarratores.publication.entities

import com.mojarras.sys.mojarratores.publication.domain.PetType
import jakarta.persistence.*

@Entity
@Table(name = "breed_info")
data class BreedInfoEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    val type: PetType,

    val breedName: String,

    @Column(length = 1000)
    val temperament: String?,

    @Column(length = 1000)
    val origin: String?,

    @Column(length = 1000)
    val lifeSpan: String?,

    @Column(length = 2000)
    val description: String?
)