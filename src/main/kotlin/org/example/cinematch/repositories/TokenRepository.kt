package org.example.cinematch.repositories

import org.example.cinematch.models.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository : JpaRepository<Token, Int> {
    fun findByUserId(userId: Int): Token?

    fun deleteByToken(token: String?)
}
