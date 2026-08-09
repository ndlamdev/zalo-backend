package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.enums.ContentMessageType
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("message")
class Message : BaseEntity() {
    @Field("conversation_id")
    var conversationId: String? = null

    @Field("sender_id")
    var senderId: String? = null

    var content: String? = null

    @Field("message_type")
    var messageType: ContentMessageType = ContentMessageType.TEXT

    @Field("reply_to_id")
    var replyToId: String? = null

    @Field("is_pinned")
    var isPinned: Boolean = false

    var statuses: Set<MessageStatus> = setOf()

    var reactions: Set<MessageReaction> = setOf()
}
