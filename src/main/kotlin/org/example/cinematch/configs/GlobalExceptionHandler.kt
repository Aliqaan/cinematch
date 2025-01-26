package org.example.cinematch.configs

import org.example.cinematch.exceptions.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    data class ErrorResponse(
        val detail: String,
    )

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse(exception.message), exception.status)

    @ExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception): ResponseEntity<ErrorResponse> {
        exception.printStackTrace()
        return ResponseEntity(
            ErrorResponse(exception.message ?: "An unexpected error occurred"),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
