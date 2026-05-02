package com.mojarras.sys.mojarratores.interest.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "interests",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["publication_id", "interested_user_id"])
    ]
)
data class InterestEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "publication_id", nullable = false)
    val publicationId: Long,

    @Column(name = "interested_user_id", nullable = false)
    val interestedUserId: Long,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)