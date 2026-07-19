package com.lamnguyen.chat.entities

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("conversation_member_metadata")
class ConversationMemberMetadata : BaseEntity() {
    @Column("conversation_id")
    var conversationId: String? = null

    @Column("member_id")
    var memberId: String? = null

    @Column("last_read_message_at")
    var lastReadMessageAt: LocalDateTime? = null

    @Column("is_pinned")
    var pinned: Boolean = false

    @Column("is_muted")
    var muted: Boolean = false

    @Column("is_archived")
    var archived: Boolean = false
}
