package com.lamnguyen.chatws.domain.messages

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.chatws.utils.enums.ContentMessageType
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChatMessage(
    val senderPhoneNumber: String,
    val roomChatId: Long,
    val content: String,
    val type: ContentMessageType,
    val urlMedia: String,
    val timestamp: Instant = Instant.now(),
)
