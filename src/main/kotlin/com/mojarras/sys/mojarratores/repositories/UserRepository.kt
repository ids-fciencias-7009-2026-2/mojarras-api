package com.mojarras.sys.mojarratores.repositories

import com.mojarras.sys.mojarratores.entities.UserEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.email = :email or u.username = :username")
    fun findByEmailOrUsername(email: String, username: String): UserEntity?

    @Query("select u from UserEntity u where u.token = :token")
    fun findByToken(token: String): UserEntity?

    @Query("select u from UserEntity u where u.email = :email")
    fun findByEmail(email: String): UserEntity?

    @Query("select u from UserEntity u where u.email = :email and u.password = :password")
    fun findUserByPasswordAndEmail(email: String, password: String): UserEntity?

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.token = :token where u.id = :id")
    fun updateTokenById(id: Long, token: String?)
}
