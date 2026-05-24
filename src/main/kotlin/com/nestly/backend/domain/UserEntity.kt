package com.nestly.backend.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @Column(length = 36)
    var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    var name: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),
)
