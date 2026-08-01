package com.lamnguyen.chatws.domain.messages

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.chatws.domain.requests.Message
import com.lamnguyen.chatws.utils.enums.ContentMessageType
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ChatMessage() : Message() {
    var senderPhoneNumber: String? = null
    var type: ContentMessageType? = ContentMessageType.TEXT
    var timestamp: Instant? = Instant.now()

    constructor(message: Message) : this() {
        this.content = message.content
        this.conversationId = message.conversationId
        this.users = message.users
        this.attachments = message.attachments
    }
}
