package org.example.cinematch.services

import jakarta.transaction.Transactional
import org.example.cinematch.models.Token
import org.example.cinematch.models.User
import org.example.cinematch.models.UserStatus
import org.example.cinematch.repositories.TokenRepository
import org.example.cinematch.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): User {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email is already in use")
        }

        val encodedPassword = passwordEncoder.encode(password)

        val user =
            User(
                email = email,
                firstName = firstName,
                lastName = lastName,
                passwordHash = encodedPassword,
                status = UserStatus.ACTIVE,
            )

        return userRepository.save(user)
    }

    fun updateLastLogin(user: User) {
        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)
    }

    fun getUserByEmail(email: String): User? = userRepository.findByEmail(email)

    fun saveRefreshToken(
        userId: Int,
        refreshToken: String,
    ) {
        val existingToken = tokenRepository.findByUserId(userId)
        val expiresAt = LocalDateTime.now().plus(7, ChronoUnit.DAYS)

        if (existingToken != null) {
            existingToken.token = refreshToken
            existingToken.expiresAt = expiresAt
            tokenRepository.save(existingToken)
        } else {
            tokenRepository.save(
                Token(
                    userId = userId,
                    token = refreshToken,
                    expiresAt = expiresAt,
                ),
            )
        }
    }

    @Transactional
    fun logout(refreshToken: String?) {
        tokenRepository.deleteByToken(refreshToken)
    }
}
