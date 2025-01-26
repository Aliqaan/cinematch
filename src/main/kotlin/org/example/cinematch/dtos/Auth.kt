package org.example.cinematch.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginResponse(
    val accessToken: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RegisterRequest(
    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "Email is required")
    val email: String,
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    @field:NotBlank(message = "Password is required")
    val password: String,
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
)
