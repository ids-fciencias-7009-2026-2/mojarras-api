package com.mojarras.sys.mojarratores.publication.entities

import com.mojarras.sys.mojarratores.publication.domain.PublicationStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "publications")
data class PublicationEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "owner_id", nullable = false)
    val ownerId: Long,

    @Column(nullable = false)
    val petName: String,

    @Column(nullable = false, length = 1000)
    val description: String,

    @Column(nullable = false)
    val type: String,

    val breed: String?,

    @Column(name = "zip_code", nullable = false)
    val zipCode: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: PublicationStatus = PublicationStatus.DRAFT,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)