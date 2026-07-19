package com.lamnguyen.chat.entities

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("message_reaction")
class MessageReaction : BaseEntity() {
    @Column("message_id")
    var messageId: String? = null

    @Column("member_id")
    var memberId: String? = null

    var emoji: String? = null
}
