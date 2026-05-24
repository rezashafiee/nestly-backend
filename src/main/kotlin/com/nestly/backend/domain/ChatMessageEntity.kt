package com.nestly.backend.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "chat_messages")
class ChatMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    var conversation: ConversationEntity = ConversationEntity(),

    @Column(name = "author_name", nullable = false)
    var authorName: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var body: String = "",

    @Column(name = "sent_at", nullable = false)
    var sentAt: Instant = Instant.now(),

    @Column(name = "from_current_user", nullable = false)
    var fromCurrentUser: Boolean = false,
)
