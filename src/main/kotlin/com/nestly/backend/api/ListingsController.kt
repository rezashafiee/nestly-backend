package com.nestly.backend.api

import com.nestly.backend.api.dto.CreateListingRequest
import com.nestly.backend.api.dto.ListingResponse
import com.nestly.backend.service.ListingService
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
@RequestMapping("/api/listings")
class ListingsController(
    private val listingService: ListingService,
) {
    @GetMapping
    fun list(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) condition: String?,
        @RequestParam(required = false) sort: String?,
    ): List<ListingResponse> {
        return listingService.list(
            query = q,
            category = category,
            condition = condition,
            sort = sort,
        )
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ListingResponse = listingService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateListingRequest,
    ): ListingResponse = listingService.create(request)
}
