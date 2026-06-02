package com.nestly.backend.service

import com.nestly.backend.domain.ListingEntity
import com.nestly.backend.domain.UserEntity
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListingMapperTest {
    private val mapper = ListingMapper()

    @Test
    fun `marks listing as mine only for matching viewer email`() {
        val listing = ListingEntity(
            id = "listing-chair",
            title = "Cozy chair",
            category = "Furniture",
            price = BigDecimal("95.00"),
            condition = "Good",
            location = "Berlin",
            description = "Comfortable chair with clean fabric.",
            seller = UserEntity(
                id = "seller-sara",
                email = "sara@example.com",
                name = "Sara",
            ),
            createdAt = Instant.parse("2026-01-01T12:00:00Z"),
            photoIconKey = "chair",
            photoColorValue = 4290241735,
            likes = 0,
            isMine = true,
        )

        val sellerView = mapper.toResponse(listing, viewerEmail = "SARA@example.com")
        val buyerView = mapper.toResponse(listing, viewerEmail = "buyer@example.com")
        val anonymousView = mapper.toResponse(listing)

        assertTrue(sellerView.isMine)
        assertFalse(buyerView.isMine)
        assertFalse(anonymousView.isMine)
        assertEquals("sara@example.com", sellerView.sellerEmail)
    }
}
