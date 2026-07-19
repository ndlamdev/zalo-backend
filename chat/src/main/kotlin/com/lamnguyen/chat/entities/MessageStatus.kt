package com.lamnguyen.chat.entities

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("message_status")
class MessageStatus : BaseEntity() {
    @Column("message_id")
    var messageId: String? = null

    @Column("member_id")
    var memberId: String? = null

    var status: Byte = 0

    @Column("status_at")
    var statusAt: LocalDateTime? = null
}
