package org.example.cinematch.controllers

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.apache.coyote.BadRequestException
import org.example.cinematch.dtos.LoginRequest
import org.example.cinematch.dtos.LoginResponse
import org.example.cinematch.dtos.RegisterRequest
import org.example.cinematch.models.UserStatus
import org.example.cinematch.services.UserService
import org.example.cinematch.utils.JwtUtils
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtUtils: JwtUtils,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse,
    ): ResponseEntity<LoginResponse> {
        val user = userService.getUserByEmail(loginRequest.email)

        if (user == null || !passwordEncoder.matches(loginRequest.password, user.passwordHash)) {
            throw BadRequestException("Incorrect email or password")
        }

        if (user.status != UserStatus.ACTIVE) {
            throw BadRequestException("Inactive User")
        }

        val accessToken = jwtUtils.generateAccessToken(user.email)
        val refreshToken = jwtUtils.generateRefreshToken(user.email)

        userService.saveRefreshToken(user.id, refreshToken)
        userService.updateLastLogin(user)

        response.addCookie(
            Cookie("refreshToken", refreshToken).apply {
                isHttpOnly = true
                secure = true
            },
        )

        return ResponseEntity.ok(LoginResponse(accessToken = accessToken))
    }

    @GetMapping("/refresh")
    fun refresh(
        @CookieValue(value = "refreshToken", required = false) refreshToken: String?,
    ): ResponseEntity<LoginResponse> {
        if (refreshToken == null) {
            throw BadRequestException("Refresh token not found")
        }

        if (!jwtUtils.validateToken(refreshToken)) {
            throw BadRequestException("Invalid refresh token")
        }

        val email = jwtUtils.extractEmail(refreshToken)
        val newAccessToken = jwtUtils.generateAccessToken(email)
        return ResponseEntity.ok(LoginResponse(accessToken = newAccessToken))
    }

    @GetMapping("/logout")
    fun logout(
        @CookieValue(value = "refreshToken", required = false) refreshToken: String?,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        userService.logout(refreshToken)

        response.addCookie(
            Cookie("refreshToken", "").apply {
                isHttpOnly = true
                secure = true
                maxAge = 0 // Delete cookie
            },
        )

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody registerRequest: RegisterRequest,
    ): ResponseEntity<Void> {
        userService.registerUser(
            registerRequest.email,
            registerRequest.password,
            registerRequest.firstName,
            registerRequest.lastName,
        )
        return ResponseEntity.noContent().build()
    }
}
