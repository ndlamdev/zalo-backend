package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.annotations.JsonDateTimeFormat
import com.lamnguyen.chat.utils.enums.ConversationType
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("conversation")
open class Conversation : BaseEntity() {
    @Column("soft_id")
    var softId: String? = null

    var admin: String? = null

    var type: ConversationType? = ConversationType.PRIVATE

    var title: String? = null

    @Column("avatar_url")
    var avatarUrl: String? = null

    @Column("theme")
    var theme: String? = null

    @Column("last_message_id")
    var lastMessageId: String? = null

    @JsonDateTimeFormat
    @Column("last_message_at")
    var lastMessageAt: LocalDateTime? = null

    @Column("is_muted")
    var isMuted: Boolean = false
}
