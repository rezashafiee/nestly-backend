package com.nestly.backend.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String,
)

data class GoogleSignInRequest(
    val email: String? = null,
    val name: String? = null,
)

data class UserProfileResponse(
    val name: String,
    val email: String,
)
