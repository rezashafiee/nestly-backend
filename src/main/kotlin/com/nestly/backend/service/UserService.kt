package com.nestly.backend.service

import com.nestly.backend.api.dto.GoogleSignInRequest
import com.nestly.backend.api.dto.UserProfileResponse
import com.nestly.backend.domain.UserEntity
import com.nestly.backend.repository.UserRepository
import com.nestly.backend.support.LockedAccountException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun signInWithEmail(email: String): UserProfileResponse {
        val normalizedEmail = normalizeEmail(email)
        if (normalizedEmail.contains("locked")) {
            throw LockedAccountException(
                "This account is temporarily locked. Try another sign-in method.",
            )
        }

        val user = findOrCreate(
            email = normalizedEmail,
            name = nameFromEmail(normalizedEmail),
        )
        return user.toResponse()
    }

    @Transactional
    fun signInWithGoogle(request: GoogleSignInRequest): UserProfileResponse {
        val email = normalizeEmail(request.email ?: "google.user@nestly.app")
        val name = request.name?.trim().takeUnless { it.isNullOrBlank() } ?: "Google User"
        return findOrCreate(email = email, name = name).toResponse()
    }

    @Transactional
    fun findOrCreate(email: String, name: String): UserEntity {
        val normalizedEmail = normalizeEmail(email)
        val cleanName = name.trim().ifBlank { nameFromEmail(normalizedEmail) }
        val existing = userRepository.findByEmailIgnoreCase(normalizedEmail)
        if (existing != null) {
            if (existing.name != cleanName && cleanName.isNotBlank()) {
                existing.name = cleanName
            }
            return existing
        }
        return userRepository.save(
            UserEntity(
                email = normalizedEmail,
                name = cleanName,
            ),
        )
    }

    private fun normalizeEmail(email: String): String = email.trim().lowercase()

    private fun nameFromEmail(email: String): String {
        val localPart = email.substringBefore("@")
            .replace(".", " ")
            .trim()

        if (localPart.isBlank()) {
            return "Nestly User"
        }

        return localPart.split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { part ->
                part.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase() else char.toString()
                }
            }
    }

    private fun UserEntity.toResponse(): UserProfileResponse {
        return UserProfileResponse(
            name = name,
            email = email,
        )
    }
}
