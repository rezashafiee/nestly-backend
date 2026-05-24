package com.nestly.backend.api

import com.nestly.backend.api.dto.GoogleSignInRequest
import com.nestly.backend.api.dto.SignInRequest
import com.nestly.backend.api.dto.UserProfileResponse
import com.nestly.backend.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/email")
    fun signInWithEmail(
        @Valid @RequestBody request: SignInRequest,
    ): UserProfileResponse = userService.signInWithEmail(request.email)

    @PostMapping("/google")
    fun signInWithGoogle(
        @RequestBody(required = false) request: GoogleSignInRequest?,
    ): UserProfileResponse = userService.signInWithGoogle(request ?: GoogleSignInRequest())
}
