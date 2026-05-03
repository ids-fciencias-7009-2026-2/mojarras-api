package com.mojarras.sys.mojarratores.exception

import org.springframework.http.*
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import tools.jackson.databind.exc.InvalidFormatException


@RestControllerAdvice
class GlobalExceptionHandler {

    // Método auxiliar para unificar el formato de respuesta
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
                "message" to "Error de validación en los campos",
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
                "Valor inválido para '$fieldName'. Opciones válidas: [$enums]"
            } else {
                "Valor inválido para el campo '$fieldName'."
            }
        } else {
            "Error de lectura: El cuerpo JSON está mal formado o contiene valores incompatibles."
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException) =
        buildResponse(HttpStatus.BAD_REQUEST, "El valor '${ex.value}' no es válido para el parámetro '${ex.name}'")
}