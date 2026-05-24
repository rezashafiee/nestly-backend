package com.nestly.backend.support

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

data class ApiError(
    val message: String,
    val details: Map<String, String> = emptyMap(),
    val timestamp: Instant = Instant.now(),
)

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(error: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val details = error.bindingResult.fieldErrors.associate { fieldError ->
            fieldError.field to (fieldError.defaultMessage ?: "Invalid value")
        }
        return response(
            status = HttpStatus.BAD_REQUEST,
            message = "Request validation failed.",
            details = details,
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(error: ConstraintViolationException): ResponseEntity<ApiError> {
        val details = error.constraintViolations.associate { violation ->
            violation.propertyPath.toString() to violation.message
        }
        return response(
            status = HttpStatus.BAD_REQUEST,
            message = "Request validation failed.",
            details = details,
        )
    }

    @ExceptionHandler(BusinessRuleException::class)
    fun handleBusinessRule(error: BusinessRuleException): ResponseEntity<ApiError> {
        return response(HttpStatus.BAD_REQUEST, error.message ?: "Request could not be processed.")
    }

    @ExceptionHandler(LockedAccountException::class)
    fun handleLockedAccount(error: LockedAccountException): ResponseEntity<ApiError> {
        return response(HttpStatus.LOCKED, error.message ?: "Account is locked.")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(error: NotFoundException): ResponseEntity<ApiError> {
        return response(HttpStatus.NOT_FOUND, error.message ?: "Resource was not found.")
    }

    private fun response(
        status: HttpStatus,
        message: String,
        details: Map<String, String> = emptyMap(),
    ): ResponseEntity<ApiError> {
        return ResponseEntity.status(status).body(
            ApiError(
                message = message,
                details = details,
            ),
        )
    }
}
