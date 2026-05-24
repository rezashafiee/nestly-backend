package com.nestly.backend.repository

import com.nestly.backend.domain.ChatMessageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long>
