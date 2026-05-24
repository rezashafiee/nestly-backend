package com.nestly.backend.service

import com.nestly.backend.api.dto.ChatMessageResponse
import com.nestly.backend.api.dto.ConversationResponse
import com.nestly.backend.api.dto.SendMessageRequest
import com.nestly.backend.api.dto.StartConversationRequest
import com.nestly.backend.domain.ChatMessageEntity
import com.nestly.backend.domain.ConversationEntity
import com.nestly.backend.repository.ChatMessageRepository
import com.nestly.backend.repository.ConversationRepository
import com.nestly.backend.support.BusinessRuleException
import com.nestly.backend.support.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ConversationService(
    private val conversationRepository: ConversationRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val listingService: ListingService,
    private val userService: UserService,
    private val listingMapper: ListingMapper,
) {
    @Transactional(readOnly = true)
    fun list(buyerEmail: String?): List<ConversationResponse> {
        val conversations = if (buyerEmail.isNullOrBlank()) {
            conversationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
        } else {
            conversationRepository.findByBuyerEmailIgnoreCaseOrderByCreatedAtDesc(
                buyerEmail.trim().lowercase(),
            )
        }

        return conversations.map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun get(id: String): ConversationResponse {
        return toResponse(findById(id))
    }

    @Transactional
    fun start(request: StartConversationRequest): ConversationResponse {
        val buyer = userService.findOrCreate(
            email = request.buyerEmail,
            name = request.buyerName,
        )
        val existing = conversationRepository.findByListingIdAndBuyerEmailIgnoreCase(
            listingId = request.listingId,
            buyerEmail = buyer.email,
        )
        if (existing != null) {
            return toResponse(existing)
        }

        val listing = listingService.findById(request.listingId)
        val conversation = ConversationEntity(
            id = "chat-${listing.id}-${buyer.id.take(8)}",
            listing = listing,
            buyer = buyer,
            createdAt = Instant.now(),
        )
        conversation.messages.add(
            ChatMessageEntity(
                conversation = conversation,
                authorName = listing.seller.name,
                body = "Hi, ${buyer.name}. Happy to answer questions about the ${listing.title}.",
                sentAt = Instant.now().minus(4, ChronoUnit.MINUTES),
                fromCurrentUser = false,
            ),
        )

        return toResponse(conversationRepository.save(conversation))
    }

    @Transactional
    fun sendMessage(conversationId: String, request: SendMessageRequest): ConversationResponse {
        val body = request.body.trim()
        if (body.isBlank()) {
            throw BusinessRuleException("Type a message before sending.")
        }

        val conversation = findById(conversationId)
        val author = userService.findOrCreate(
            email = request.authorEmail,
            name = request.authorName,
        )
        val message = ChatMessageEntity(
            conversation = conversation,
            authorName = author.name,
            body = body,
            sentAt = Instant.now(),
            fromCurrentUser = true,
        )
        conversation.messages.add(chatMessageRepository.save(message))

        return toResponse(conversation)
    }

    private fun findById(id: String): ConversationEntity {
        return conversationRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Choose a conversation first.")
    }

    private fun toResponse(conversation: ConversationEntity): ConversationResponse {
        return ConversationResponse(
            id = conversation.id,
            listing = listingMapper.toResponse(conversation.listing),
            messages = conversation.messages.map { message ->
                ChatMessageResponse(
                    authorName = message.authorName,
                    body = message.body,
                    sentAt = message.sentAt,
                    fromCurrentUser = message.fromCurrentUser,
                )
            },
        )
    }
}
