package com.nestly.backend.service

import com.nestly.backend.api.dto.ListingResponse
import com.nestly.backend.api.dto.PhotoAssetDto
import com.nestly.backend.domain.ListingEntity
import org.springframework.stereotype.Component

@Component
class ListingMapper {
    fun toResponse(listing: ListingEntity, viewerEmail: String? = null): ListingResponse {
        val sellerEmail = listing.seller.email
        return ListingResponse(
            id = listing.id,
            title = listing.title,
            category = listing.category,
            price = listing.price,
            condition = listing.condition,
            location = listing.location,
            description = listing.description,
            sellerName = listing.seller.name,
            sellerEmail = sellerEmail,
            createdAt = listing.createdAt,
            photo = PhotoAssetDto(
                iconKey = listing.photoIconKey,
                colorValue = listing.photoColorValue,
            ),
            likes = listing.likes,
            isMine = viewerEmail?.trim()?.equals(sellerEmail, ignoreCase = true) == true,
        )
    }
}
