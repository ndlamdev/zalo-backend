package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.entities.Message
import org.springframework.data.mongodb.core.mapping.Field

class ConversationDto : Conversation() {
    var pinned: Boolean = false
    @Field("last_message")
    var lastMessage: Message? = null
    @Field("total_message_unread")
    var totalMessageUnread: Int = 0
}
