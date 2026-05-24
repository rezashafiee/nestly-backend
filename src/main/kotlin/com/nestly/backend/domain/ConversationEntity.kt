package com.nestly.backend.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "conversations")
class ConversationEntity(
    @Id
    @Column(length = 96)
    var id: String = "",

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id", nullable = false)
    var listing: ListingEntity = ListingEntity(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buyer_id", nullable = false)
    var buyer: UserEntity = UserEntity(),

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @OneToMany(
        mappedBy = "conversation",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @OrderBy("sentAt ASC, id ASC")
    var messages: MutableList<ChatMessageEntity> = mutableListOf(),
)
