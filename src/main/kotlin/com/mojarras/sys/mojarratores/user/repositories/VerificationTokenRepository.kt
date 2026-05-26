package com.mojarras.sys.mojarratores.user.repositories

import com.mojarras.sys.mojarratores.user.entities.VerificationTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationTokenRepository : JpaRepository<VerificationTokenEntity, Long> {
    fun findByToken(token: String): VerificationTokenEntity?
}