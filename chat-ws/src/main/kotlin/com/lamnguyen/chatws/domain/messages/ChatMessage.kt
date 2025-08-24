package com.lamnguyen.chatws.domain.messages

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.chatws.domain.requests.TextMessage
import com.lamnguyen.chatws.utils.enums.ContentMessageType
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ChatMessage() : TextMessage() {
    var senderPhoneNumber: String? = null
    var type: ContentMessageType? = ContentMessageType.TEXT
    var timestamp: Instant? = Instant.now()

    constructor(message: TextMessage) : this() {
        this.content = message.content
        this.roomChatId = message.roomChatId
    }
}
