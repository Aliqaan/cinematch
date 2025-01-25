package org.example.cinematch.dtos

data class LoginResponse(
    val accessToken: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)
