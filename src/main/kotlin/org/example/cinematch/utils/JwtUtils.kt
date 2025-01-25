package org.example.cinematch.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils(
    @Value("\${jwt.secret}") private val secretString: String,
) {
    private val secretKey: Key =
        SecretKeySpec(
            secretString.toByteArray(),
            SignatureAlgorithm.HS256.jcaName,
        )

    fun generateAccessToken(email: String): String {
        val expirationTime = 3600000L // 1 hour for access token
        return Jwts
            .builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(email: String): String {
        val expirationTime = 604800000L // 7 days for refresh token
        return Jwts
            .builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean =
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }

    fun extractEmail(token: String): String =
        Jwts
            .parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body.subject
}
