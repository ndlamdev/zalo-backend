package com.ndlamdev.chatwsrouter.domain.message

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.ndlamdev.chatwsrouter.utils.enums.ContentMessageType
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ReceiveMessage {
    var content: String? = null
    var user: String? = null
    var attachments: List<Any> = emptyList()
    var senderPhoneNumber: String? = null
    var type: ContentMessageType? = ContentMessageType.TEXT
    var timestamp: Instant? = Instant.now()
}
