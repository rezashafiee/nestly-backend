package com.nestly.backend.service

import com.nestly.backend.api.dto.PhotoAssetDto
import org.springframework.stereotype.Service

@Service
class PhotoAssetService {
    fun forCategory(category: String): PhotoAssetDto {
        return when (category.trim()) {
            "Electronics" -> PhotoAssetDto(iconKey = "electronics", colorValue = 0xFFCDB4DB)
            "Home" -> PhotoAssetDto(iconKey = "lamp", colorValue = 0xFFFFC6D9)
            "Sports" -> PhotoAssetDto(iconKey = "bike", colorValue = 0xFFFFD6A5)
            "Clothing" -> PhotoAssetDto(iconKey = "clothing", colorValue = 0xFFA0C4FF)
            else -> PhotoAssetDto(iconKey = "chair", colorValue = 0xFFB7E4C7)
        }
    }
}
