package com.ndlamdev.chatwsrouter.domain.dto

import com.ndlamdev.chatwsrouter.domain.message.ChatMessage
import com.ndlamdev.chatwsrouter.utils.enums.ContentMessageType
import java.time.Instant

class Message {
    var content: String? = null
    var user: String = ""
    var attachments: List<Any> = emptyList()
    var senderPhoneNumber: String? = null
    var type: ContentMessageType? = ContentMessageType.TEXT
    var timestamp: Instant? = Instant.now()

    companion object {
        fun parse(user: String, message: ChatMessage): Message {
            return Message().apply {
                this.user = user
                content = message.content
                attachments = message.attachments
                senderPhoneNumber = message.senderPhoneNumber
                type = ContentMessageType.TEXT
                timestamp = message.timestamp
            }
        }
    }
}