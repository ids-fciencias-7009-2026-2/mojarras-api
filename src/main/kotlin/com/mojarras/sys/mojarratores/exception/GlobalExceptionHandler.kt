package com.mojarras.sys.mojarratores.exception

import org.springframework.http.*
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException) =
        ResponseEntity.status(HttpStatus.CONFLICT)
            .body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException) =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {

        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "invalid") }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }
}