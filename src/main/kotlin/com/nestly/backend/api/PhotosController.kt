package com.nestly.backend.api

import com.nestly.backend.api.dto.PhotoAssetDto
import com.nestly.backend.service.PhotoAssetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/photos")
class PhotosController(
    private val photoAssetService: PhotoAssetService,
) {
    @GetMapping("/default")
    fun defaultForCategory(
        @RequestParam category: String,
    ): PhotoAssetDto = photoAssetService.forCategory(category)
}
