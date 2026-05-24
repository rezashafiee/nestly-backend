package com.nestly.backend.repository

import com.nestly.backend.domain.ConversationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<ConversationEntity, String> {
    fun findByBuyerEmailIgnoreCaseOrderByCreatedAtDesc(email: String): List<ConversationEntity>

    fun findByListingIdAndBuyerEmailIgnoreCase(
        listingId: String,
        buyerEmail: String,
    ): ConversationEntity?
}
