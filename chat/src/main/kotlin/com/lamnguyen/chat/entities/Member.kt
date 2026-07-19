package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.enums.MemberRole
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("member")
class Member : BaseEntity() {
    @Column("conversation_id")
    var conversationId: String? = null

    @Column("user_id")
    var userId: String? = null

    var role: MemberRole = MemberRole.USER

    @Column("joined_at")
    var joinedAt: LocalDateTime? = null

    @Column("joined_by")
    var joinedBy: String? = null

    @Column("is_muted")
    var muted: Boolean = false

    @Column("is_active")
    var active: Boolean = true

    @Transient
    var metadata: ConversationMemberMetadata? = null
}
