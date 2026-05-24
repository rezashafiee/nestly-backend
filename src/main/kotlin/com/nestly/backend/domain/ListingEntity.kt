package com.nestly.backend.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "listings")
class ListingEntity(
    @Id
    @Column(length = 64)
    var id: String = "",

    @Column(nullable = false, length = 160)
    var title: String = "",

    @Column(nullable = false, length = 80)
    var category: String = "",

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(name = "item_condition", nullable = false, length = 80)
    var condition: String = "",

    @Column(nullable = false, length = 160)
    var location: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String = "",

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: UserEntity = UserEntity(),

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "photo_icon_key", nullable = false, length = 80)
    var photoIconKey: String = "",

    @Column(name = "photo_color_value", nullable = false)
    var photoColorValue: Long = 0,

    @Column(nullable = false)
    var likes: Int = 0,

    @Column(name = "is_mine", nullable = false)
    var isMine: Boolean = false,
)
