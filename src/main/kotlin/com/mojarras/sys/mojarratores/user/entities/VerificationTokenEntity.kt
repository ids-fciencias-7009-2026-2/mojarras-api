package com.mojarras.sys.mojarratores.user.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "verification_tokens")
data class VerificationTokenEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val token:String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "expiry_date", nullable = false)
    val expiryDate: LocalDateTime,

    @Column(name= "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)