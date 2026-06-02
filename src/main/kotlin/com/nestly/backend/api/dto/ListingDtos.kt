package com.nestly.backend.api.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.time.Instant

data class PhotoAssetDto(
    val iconKey: String,
    val colorValue: Long,
)

data class ListingResponse(
    val id: String,
    val title: String,
    val category: String,
    val price: BigDecimal,
    val condition: String,
    val location: String,
    val description: String,
    val sellerName: String,
    val sellerEmail: String,
    val createdAt: Instant,
    val photo: PhotoAssetDto,
    val likes: Int,
    val isMine: Boolean,
)

data class CreateListingRequest(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val category: String,

    @field:DecimalMin("0.01")
    @field:DecimalMax("50000.00")
    val price: BigDecimal,

    @field:NotBlank
    val condition: String,

    @field:NotBlank
    val location: String,

    @field:NotBlank
    val description: String,

    @field:NotBlank
    val sellerName: String,

    @field:NotBlank
    val sellerEmail: String,

    @field:Valid
    val photo: PhotoAssetDto? = null,
)
