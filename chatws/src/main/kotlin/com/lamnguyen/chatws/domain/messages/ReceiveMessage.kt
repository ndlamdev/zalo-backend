package com.lamnguyen.chatws.domain.messages

import com.lamnguyen.chatws.utils.enums.ContentMessageType
import java.time.Instant

class ReceiveMessage {
    var content: String? = null
    var user: String = ""
    var attachments: List<Any> = emptyList()
    var senderPhoneNumber: String? = null
    var type: ContentMessageType? = ContentMessageType.TEXT
    var timestamp: Instant? = Instant.now()
}