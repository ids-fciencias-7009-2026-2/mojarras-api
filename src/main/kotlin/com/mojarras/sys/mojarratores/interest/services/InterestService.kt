package com.mojarras.sys.mojarratores.interest.services

import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.exception.ConflictException
import com.mojarras.sys.mojarratores.exception.NotFoundException
import com.mojarras.sys.mojarratores.exception.UnauthorizedException
import com.mojarras.sys.mojarratores.infrastructure.EmailService
import com.mojarras.sys.mojarratores.interest.domain.Interest
import com.mojarras.sys.mojarratores.interest.dto.response.InterestedUserResponse
import com.mojarras.sys.mojarratores.interest.mapper.toInterestEntity
import com.mojarras.sys.mojarratores.interest.repositories.InterestRepository
import com.mojarras.sys.mojarratores.publication.repositories.PublicationRepository
import com.mojarras.sys.mojarratores.user.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InterestService(
    private val interestRepository: InterestRepository,
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    private val emailService: EmailService
) {
    private val logger = LoggerFactory.getLogger(InterestService::class.java)

    fun markInterest(publicationId: Long, email: String) {

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        val publication = publicationRepository.findById(publicationId)
            .orElseThrow { NotFoundException("Publication not found") }

        if (publication.ownerId == user.id) {
            throw BadRequestException("You cannot mark interest in your own publication")
        }

        if (interestRepository.existsByPublicationIdAndInterestedUserId(publicationId, user.id!!)) {
            throw ConflictException("You already marked interest")
        }

        val interest = Interest(
            publicationId = publicationId,
            interestedUserId = user.id
        )

        interestRepository.save(interest.toInterestEntity())

        val owner = userRepository.findById(publication.ownerId)
            .orElseThrow { NotFoundException("Owner not found") }

        emailService.sendInterestNotification(
            ownerEmail = owner.email,
            ownerName = owner.firstName,
            interestedName = user.firstName,
            interestedEmail = user.email,
            petName = publication.petName
        )

        logger.info("Interest registered: user ${user.email} -> publication $publicationId")
    }


    fun getInterestedUsers(publicationId: Long, email: String): List<InterestedUserResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("User not found")

        val publication = publicationRepository.findById(publicationId)
            .orElseThrow { NotFoundException("Publication not found") }


        if (publication.ownerId != user.id) {
            throw UnauthorizedException("Not your publication")
        }

        val interests = interestRepository.findAllByPublicationId(publicationId)

        return interests.map {
            val interestedUser = userRepository.findById(it.interestedUserId)
                .orElseThrow { NotFoundException("User not found") }

            InterestedUserResponse(
                id = interestedUser.id!!,
                email = interestedUser.email,
                username = interestedUser.username
            )
        }
    }


}