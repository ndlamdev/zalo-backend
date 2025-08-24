package com.lamnguyen.chatws.domain.messages

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.chatws.utils.enums.ContentMessageType
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ChatMessage {
    var senderPhoneNumber: String? = null
    var roomChatId: Long = 0
    var content: String? = null
    var type: ContentMessageType? = ContentMessageType.TEXT
    var timestamp: Instant? = Instant.now()
}
