package org.example.cinematch.configs

import org.example.cinematch.exceptions.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    data class ErrorResponse(
        val detail: String,
    )

    data class ValidationErrorResponse(
        val detail: String,
        val errors: List<FieldErrorDetail>,
    )

    data class FieldErrorDetail(
        val field: String,
        val message: String,
    )

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse(exception.message), exception.status)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors =
            exception.bindingResult.allErrors.mapNotNull { error ->
                if (error is FieldError) {
                    FieldErrorDetail(
                        field = error.field,
                        message = error.defaultMessage ?: "Invalid value",
                    )
                } else {
                    null
                }
            }
        return ResponseEntity(
            ValidationErrorResponse("Validation failed", errors),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception): ResponseEntity<ErrorResponse> {
        exception.printStackTrace()
        return ResponseEntity(
            ErrorResponse(exception.message ?: "An unexpected error occurred"),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
