package org.example.cinematch.services

import jakarta.transaction.Transactional
import org.example.cinematch.models.Token
import org.example.cinematch.models.User
import org.example.cinematch.repositories.TokenRepository
import org.example.cinematch.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
) {
    // Create a new user and store in the database
//    fun createUser(
//        email: String,
//        password: String,
//        username: String,
//        status: String,
//    ): User {
//        // Check if email or username already exists
//        if (userRepository.existsByEmail(email)) {
//            throw IllegalArgumentException("Email is already in use")
//        }
//
//        if (userRepository.existsByUsername(username)) {
//            throw IllegalArgumentException("Username is already in use")
//        }
//
//        // Encode password
//        val encodedPassword = passwordEncoder.encode(password)
//
//        // Create a new User object
//        val user =
//            User(
//                email = email,
//                passwordHash = encodedPassword,
//                username = username,
//                status = status,
//                createdAt = Date(),
//                updatedAt = Date(),
//            )
//
//        // Save user to the database
//        return userRepository.save(user)
//    }

//    fun getUserById(userId: Long): User = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", userId) }

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
