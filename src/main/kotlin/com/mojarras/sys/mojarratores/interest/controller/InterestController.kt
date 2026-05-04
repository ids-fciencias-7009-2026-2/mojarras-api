package com.mojarras.sys.mojarratores.interest.controller

import com.mojarras.sys.mojarratores.interest.dto.response.InterestResponse
import com.mojarras.sys.mojarratores.interest.dto.response.InterestedUserResponse
import com.mojarras.sys.mojarratores.interest.services.InterestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/publications/{id}/interest")
class InterestController(
    private val interestService: InterestService
) {

    @PostMapping
    fun markInterest(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<InterestResponse> {

        interestService.markInterest(id, authentication.name)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(InterestResponse("Interest sent successfully"))
    }

    @GetMapping
    fun getInterested(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<List<InterestedUserResponse>> {

        val users = interestService.getInterestedUsers(id, authentication.name)

        return ResponseEntity.ok(users)
    }
}