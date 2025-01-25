package org.example.cinematch.repositories

import org.example.cinematch.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
}
