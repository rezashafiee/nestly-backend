package com.nestly.backend.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.Instant

data class ConversationResponse(
    val id: String,
    val listing: ListingResponse,
    val messages: List<ChatMessageResponse>,
)

data class ChatMessageResponse(
    val authorName: String,
    val body: String,
    val sentAt: Instant,
    val fromCurrentUser: Boolean,
)

data class StartConversationRequest(
    @field:NotBlank
    val listingId: String,

    @field:Email
    @field:NotBlank
    val buyerEmail: String,

    @field:NotBlank
    val buyerName: String,
)

data class SendMessageRequest(
    @field:Email
    @field:NotBlank
    val authorEmail: String,

    @field:NotBlank
    val authorName: String,

    @field:NotBlank
    val body: String,
)
