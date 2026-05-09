package com.mojarras.sys.mojarratores.exception

import org.springframework.http.*
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import tools.jackson.databind.exc.InvalidFormatException


@RestControllerAdvice
class GlobalExceptionHandler {

    private fun buildResponse(status: HttpStatus, message: String): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.status(status).body(
            mapOf(
                "timestamp" to java.time.LocalDateTime.now().toString(),
                "status" to status.value(),
                "error" to status.reasonPhrase,
                "message" to message
            )
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException) =
        buildResponse(HttpStatus.BAD_REQUEST, ex.message.orEmpty())

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException) =
        buildResponse(HttpStatus.CONFLICT, ex.message.orEmpty())

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException) =
        buildResponse(HttpStatus.NOT_FOUND, ex.message.orEmpty())

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException) =
        buildResponse(HttpStatus.UNAUTHORIZED, ex.message.orEmpty())

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "invalid") }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            mapOf(
                "timestamp" to java.time.LocalDateTime.now().toString(),
                "status" to HttpStatus.BAD_REQUEST.value(),
                "error" to "Bad Request",
                "message" to "Validation error in the fields",
                "validation_errors" to errors
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Map<String, Any>> {
        val cause = ex.cause

        val message = if (cause is InvalidFormatException) {
            val fieldName = cause.path.lastOrNull()?.propertyName ?: "campo"

            val targetType = cause.targetType
            val enums = if (targetType.isEnum) {
                targetType.enumConstants?.joinToString(", ")
            } else {
                null
            }

            if (enums != null) {
                "Invalid value for '$fieldName'. Valid options: [$enums]"
            } else {
                "Invalid value for field '$fieldName'."
            }
        } else {
            "Read error: The JSON body is malformed or contains incompatible values."
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException) =
        buildResponse(HttpStatus.BAD_REQUEST, "The value '${ex.value}' is not valid for parameter '${ex.name}'")

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSize(ex: MaxUploadSizeExceededException) =
        buildResponse(
            HttpStatus.PAYLOAD_TOO_LARGE, "Upload size exceeded. Maximum allowed is 6MB per file and 30MB per request.")

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception) =
        buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred: ${ex.message ?: "No details available"}")
}