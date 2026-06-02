package com.nestly.backend.service

import com.nestly.backend.api.dto.CreateListingRequest
import com.nestly.backend.api.dto.ListingResponse
import com.nestly.backend.domain.ListingEntity
import com.nestly.backend.repository.ListingRepository
import com.nestly.backend.support.BusinessRuleException
import com.nestly.backend.support.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class ListingService(
    private val listingRepository: ListingRepository,
    private val userService: UserService,
    private val photoAssetService: PhotoAssetService,
    private val listingMapper: ListingMapper,
) {
    @Transactional(readOnly = true)
    fun list(
        query: String?,
        category: String?,
        condition: String?,
        sort: String?,
        viewerEmail: String?,
    ): List<ListingResponse> {
        val search = query?.trim()?.lowercase().orEmpty()
        val categoryFilter = category?.trim().takeUnless { it.isNullOrBlank() || it == "All" }
        val conditionFilter = condition?.trim().takeUnless { it.isNullOrBlank() || it == "Any" }

        val filtered = listingRepository.findAll()
            .asSequence()
            .filter { listing ->
                search.isBlank() ||
                    listing.title.contains(search, ignoreCase = true) ||
                    listing.description.contains(search, ignoreCase = true) ||
                    listing.location.contains(search, ignoreCase = true) ||
                    listing.category.contains(search, ignoreCase = true)
            }
            .filter { listing -> categoryFilter == null || listing.category == categoryFilter }
            .filter { listing -> conditionFilter == null || listing.condition == conditionFilter }
            .toList()

        return sort(filtered, sort).map { listing ->
            listingMapper.toResponse(listing, viewerEmail)
        }
    }

    @Transactional(readOnly = true)
    fun get(id: String, viewerEmail: String?): ListingResponse {
        return listingMapper.toResponse(findById(id), viewerEmail)
    }

    @Transactional
    fun create(request: CreateListingRequest): ListingResponse {
        if (request.price > MAX_LISTING_PRICE) {
            throw BusinessRuleException(
                "That price looks unusually high. Please review it before publishing.",
            )
        }

        val seller = userService.findOrCreate(
            email = request.sellerEmail,
            name = request.sellerName,
        )
        val photo = request.photo ?: photoAssetService.forCategory(request.category)
        val listing = ListingEntity(
            id = "listing-${UUID.randomUUID()}",
            title = request.title.trim(),
            category = request.category.trim(),
            price = request.price,
            condition = request.condition.trim(),
            location = request.location.trim(),
            description = request.description.trim(),
            seller = seller,
            createdAt = Instant.now(),
            photoIconKey = photo.iconKey,
            photoColorValue = photo.colorValue,
            likes = 0,
        )

        return listingMapper.toResponse(
            listingRepository.save(listing),
            viewerEmail = request.sellerEmail,
        )
    }

    @Transactional(readOnly = true)
    fun findById(id: String): ListingEntity {
        return listingRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Listing '$id' was not found.")
    }

    private fun sort(listings: List<ListingEntity>, sort: String?): List<ListingEntity> {
        return when (sort?.trim()?.lowercase()) {
            "price_low", "price-low", "priceLow".lowercase() -> listings.sortedBy { it.price }
            "price_high", "price-high", "priceHigh".lowercase() -> listings.sortedByDescending { it.price }
            "popular" -> listings.sortedByDescending { it.likes }
            else -> listings.sortedByDescending { it.createdAt }
        }
    }

    private companion object {
        val MAX_LISTING_PRICE: BigDecimal = BigDecimal("50000.00")
    }
}
