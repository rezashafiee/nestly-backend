package com.nestly.backend.api

import com.nestly.backend.api.dto.ConversationResponse
import com.nestly.backend.api.dto.SendMessageRequest
import com.nestly.backend.api.dto.StartConversationRequest
import com.nestly.backend.service.ConversationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/conversations")
class ConversationsController(
    private val conversationService: ConversationService,
) {
    @GetMapping
    fun list(
        @RequestParam(required = false) buyerEmail: String?,
    ): List<ConversationResponse> = conversationService.list(buyerEmail)

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ConversationResponse = conversationService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun start(
        @Valid @RequestBody request: StartConversationRequest,
    ): ConversationResponse = conversationService.start(request)

    @PostMapping("/{id}/messages")
    fun sendMessage(
        @PathVariable id: String,
        @Valid @RequestBody request: SendMessageRequest,
    ): ConversationResponse = conversationService.sendMessage(
        conversationId = id,
        request = request,
    )
}
