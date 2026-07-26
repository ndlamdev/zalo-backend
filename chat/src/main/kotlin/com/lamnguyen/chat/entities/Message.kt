package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.enums.ContentMessageType
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("message")
class Message : BaseEntity() {
    @Column("conversation_id")
    var conversationId: String? = null

    @Column("sender_id")
    var senderId: String? = null

    var content: String? = null

    @Column("message_type")
    var messageType: ContentMessageType = ContentMessageType.TEXT

    @Column("reply_to_id")
    var replyToId: String? = null

    @Column("is_pinned")
    var isPinned: Boolean = false
}
