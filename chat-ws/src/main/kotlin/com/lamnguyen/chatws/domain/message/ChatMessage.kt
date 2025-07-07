package com.lamnguyen.chatws.domain.message

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChatMessage(
    val senderId: String,
    val roomChatId: String,
    val content: String,
    val timestamp: Instant = Instant.now(),
)
