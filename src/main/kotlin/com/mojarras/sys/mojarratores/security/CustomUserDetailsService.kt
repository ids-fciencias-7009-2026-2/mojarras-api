package com.mojarras.sys.mojarratores.security

import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.springframework.security.core.userdetails.*
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {

        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")

        return User(
            user.email,
            user.passwordHash,
            emptyList()
        )
    }
}