package com.nestly.backend.repository

import com.nestly.backend.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByEmailIgnoreCase(email: String): UserEntity?
}
