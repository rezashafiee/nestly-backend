package com.nestly.backend.repository

import com.nestly.backend.domain.ListingEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ListingRepository : JpaRepository<ListingEntity, String>
